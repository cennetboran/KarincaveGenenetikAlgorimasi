/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demirci.ant;

/**
 *
 * @author ulasd
 */
public class GA {
public double[][] uzaklikMatrisi;
      
    void hesapla() {
         Hesaplama hsp = new Hesaplama();
        uzaklikMatrisi = new double[SCREEN.towns.length+1][SCREEN.towns.length+1];
        //uzaklikMatrisi = hsp.uzaklikmatrisi(SCREEN.koordinatlar);
        int runs = Integer.parseInt(SCREEN.jTextField7.getText());
        Path [] bests = new Path[runs];
 		for(int i=1; i<=SCREEN.towns.length;i++){
 			for(int j=1; j<=SCREEN.towns.length;j++){
 				uzaklikMatrisi[i][j] = SCREEN.towns[i-1].distanceTo(SCREEN.towns[j-1]);//uzaklık matisi
 			}
 		}
         for(int i = 0; i<runs; i++){
             Population population = new Population(uzaklikMatrisi, SCREEN.towns,Double.parseDouble(SCREEN.jTextField6.getText()), 10000);
               while(population.since_change < 100){

	  			population.naturalSelection();
	  			population.generate();
	                        population.calcFitness();
               }
	  		bests[i] = population.all_time;
 		}

 		Path best = bests[0];
 		System.out.println("\nbests [0]: "+best.distance);
                SCREEN.jTextArea1.append("best"+best.distance);
                System.out.println("\n path \n"+best.makeString());
                 SCREEN.jTextArea1.append("\n path \n"+best.makeString());
 		for(int i=1; i<runs; i++){
 			System.out.println("bests ["+i+"]: "+bests[i].distance);
                        SCREEN.jTextArea1.append("\nbests ["+i+"]: "+bests[i].distance);
                        System.out.println("\n path \n"+bests[i].makeString());
                        SCREEN.jTextArea1.append("\n path \n"+bests[i].makeString());
 			if(bests[i].distance < best.distance){
 				best = bests[i];
 			}
 		}

 		System.out.println("\ntotal best: \n"+best.makeString()+"\nwith a distance of: "+best.distance);
                SCREEN.jTextArea2.append("best  total"+best.distance);
                SCREEN.jTextArea2.append("\n path \n"+best.makeString());
                best.ciz();
 		//double secs_taken = (System.currentTimeMillis() - start)/1000;
 		//System.out.println("\nfinished in "+(int)(secs_taken/60)+" mins "+(int)(secs_taken%60)+" secs");
         }
}
