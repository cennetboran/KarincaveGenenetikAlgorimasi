/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demirci.ant;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ulasd
 */
public class Population{
	
	private double mutationRate;
	public Path [] population;
	private ArrayList <Path> matingPool;
	public int generations;
	public int since_change;
	public Path all_time;
	public double [][] adjacencyMatrix;
	public Town [] towns;
	public Population(double [][] adjacencyMatrix, Town [] towns, double mutationRate, int num){
		this.mutationRate = mutationRate;
		this.adjacencyMatrix = adjacencyMatrix;
		this.towns = towns;
		population = new Path [num]; 
		for(int i=0; i<num; i++){

			Path temp = new Path(adjacencyMatrix, towns);
			temp.randomPath();
			population[i] = temp;
		}

		since_change = 0;
		calcFitness();

		matingPool = new ArrayList <Path> ();
		all_time = new Path();
		all_time.distance = 100000;

	}
	public void calcFitness(){
	
		ArrayList dists = new ArrayList();
		for(int i=0; i<population.length; i++){
			Double dist = new Double(1/population[i].distance);
    		if(!dists.contains(dist)){
    			dists.add(dist);
    		}	
		}
    	double [] ascending_dists = new double[dists.size()];
    	for(int i=0; i<ascending_dists.length; i++){
    		Double temp = (Double)dists.get(i);
    		ascending_dists[i] = temp.doubleValue();
    	}

    	Arrays.sort(ascending_dists);
    	
		for(int i=0; i<population.length; i++){

			for(int j=0; j<ascending_dists.length; j++){
				if((1/population[i].distance) == ascending_dists[j]){
					population[i].fitness = Math.pow(j,4);
					break;
				}
			}
			
		}
	}

	

	public void naturalSelection(){
    	matingPool.clear();
    	Path generation_best = new Path();
    	double maxFitness = 0;
    	double totalFitness = 0.0;

    	double gen_best = 100000; 
    	for (int i = 0; i < population.length; i++) {
    		totalFitness += population[i].fitness;
    		if(gen_best > population[i].distance){
    			generation_best = population[i];
    			gen_best = generation_best.distance;
    		}
      		if (population[i].fitness > maxFitness) {
        		maxFitness = population[i].fitness;
      		}
    	}


    	if(generation_best.distance < all_time.distance){
    		all_time = generation_best;
    		since_change = 0;
    	}else{
    		since_change++;
    	}

	    for (int i = 0; i < population.length; i++) {

	    	double fitness = population[i].fitness/maxFitness; 	

	      	int n = (int)(fitness * 1000);  	
	      	for (int j = 0; j < n; j++) {         
	        	matingPool.add(population[i]);
	      	}
	    }

	} 

	public void generate(){
		population[0] = all_time;
	    for (int i = 1; i < population.length; i++) {

	    	int a = (int)(Math.random()*matingPool.size());
	    	int b = (int)(Math.random()*matingPool.size());
	    	Path partnerA = (Path) matingPool.get(a);
	    	Path partnerB = (Path) matingPool.get(b);
	    	Path child = new Path(adjacencyMatrix, towns, partnerA.crossOver(partnerB));
	    		child.mutate(mutationRate);
	    	population[i] = child;	
	
	    }
	    generations++;

	}
}