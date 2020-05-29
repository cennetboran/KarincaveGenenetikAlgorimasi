/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demirci.ant;

import static com.demirci.ant.Koloni.grafik;
import java.util.ArrayList;

/**
 *
 * @author ulasd
 */
public class Path {

    public double distance;
    public Town[] towns;
    public Town[] allTowns;
    private int size;
    private double[][] matrix;
    public double fitness;

    public Path(double[][] matrix, Town[] allTowns) {
        this.matrix = matrix;
        this.allTowns = allTowns;
        towns = new Town[matrix.length];
        distance = 0.0;
        size = 0;
        fitness = 0.0;
    }

    public Path(double[][] matrix, Town[] allTowns, Town[] inOrder) {
        this.matrix = matrix;
        this.allTowns = allTowns;
        towns = inOrder;
        distance = 0.0;
        size = 0;
        fitness = 0;
        distance = calcDistance();
        size = towns.length;
    }

    public Path() {
    }

    public String makeString() {
        String str = "";

        for (int i = 0; i < size; i++) {
            str += towns[i].id + ".";
        }

        return str;
    }

    public void ciz() {
        int enIyiTur[] = new int[size - 1];
        for (int i = 0; i < size - 1; i++) {
            enIyiTur[i] = towns[i].id - 1;
        }
        SCREEN.kenarCiz2(grafik, enIyiTur);
    }

    public void mutate(double mutationRate) {
        if (Math.random() < mutationRate) {

            int a = (int) (Math.random() * (size - 5) + 1);
            int b = (int) (Math.random() * (size - 5) + 1);
            Town[] subTowns = new Town[3];
            for (int i = 0; i < subTowns.length; i++) {
                subTowns[i] = towns[a + i];
            }
            if (a < b) {
                for (int i = a; i < b; i++) {
                    towns[i] = towns[i + 3];
                }
            } else {
                for (int i = a; i > b; i--) {
                    towns[i + 2] = towns[i - 1];
                }
            }
            for (int i = b, j = subTowns.length - 1; i < b + subTowns.length; i++, j--) {
                towns[i] = subTowns[j];
            }
            distance = calcDistance();
        }
    }
    public double calcDistance() {
        double result = 0.0;
        for (int i = 0; i < towns.length - 1; i++) {
            result += matrix[towns[i].id][towns[i + 1].id];
        }
        return result;
    }
    public void randomPath() {
        boolean[] visited = new boolean[allTowns.length];
        int added = 0;
        while (added < allTowns.length) {
            int rand = (int) (Math.random() * allTowns.length);
            if (!visited[rand]) {
                towns[added] = allTowns[rand];
                visited[rand] = true;
                added++;
            }
        }
        towns[added] = towns[0];
        distance = calcDistance();
        size = towns.length;
    }

    public Town[] crossOver(Path partner) {

        try {

            ArrayList[] neighbours = getNeighbours();
            ArrayList[] p_neighbours = partner.getNeighbours();
            ArrayList[] union = new ArrayList[towns.length];

            for (int i = 1; i < neighbours.length; i++) {
                ArrayList temp = new ArrayList(4);

                for (int j = 0; j < 2; j++) {
                    Integer candidate = (Integer) neighbours[i].get(j);
                    temp.add(candidate);
                }

                for (int j = 0; j < 2; j++) {
                    Integer candidate = (Integer) p_neighbours[i].get(j);
                    if (!temp.contains(candidate)) {
                        temp.add(candidate);
                    }
                }
                union[i] = temp;
            }
            ArrayList<Integer> list = new ArrayList<Integer>();
            Integer n = (Math.random() < Double.parseDouble(SCREEN.jTextField6.getText())) ? new Integer(towns[0].id) : new Integer(partner.towns[0].id);
            while (list.size() < towns.length - 1) {
                list.add(n);
                for (int i = 1; i < union.length; i++) {
                    boolean removed = union[i].remove(n);
                }
                int n_int = n.intValue();
                ArrayList find_n = union[n_int];
                if (find_n.size() > 0) {
                    Integer leastI = (Integer) find_n.get(0);
                    int least_size = union[leastI.intValue()].size();
                    for (int i = 1; i < find_n.size(); i++) {
                        Integer testI = (Integer) find_n.get(i);
                        if (union[testI.intValue()].size() < least_size) {
                            least_size = union[testI.intValue()].size();
                            leastI = (Integer) find_n.get(i);
                        } else if (union[testI.intValue()].size() == least_size) {
                            if (Math.random() < 0.5) {
                                leastI = (Integer) find_n.get(i);
                            }
                        }
                    }
                    n = leastI;
                } else {
                    double least = 100000;
                    Integer choose = new Integer(1);

                    for (int i = 1; i < towns.length; i++) {
                        Integer temp = new Integer(i);
                        if (!list.contains(temp) && matrix[towns[i].id][towns[n_int].id] < least) {
                            least = matrix[towns[i].id][towns[n_int].id];
                            choose = temp;
                        }
                    }
                    n = choose;
                }

            }
            Integer first = (Integer) list.get(0);
            list.add(first); //join it up!

            Town[] newTowns = new Town[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Integer temp = (Integer) list.get(i);
                newTowns[i] = allTowns[temp.intValue() - 1];
            }
            return newTowns;
        } catch (Exception e) {
            System.out.println(e + " caught while towns = \n" + makeString());
            return towns;
        }

    }

    private ArrayList[] getNeighbours() {

        ArrayList[] lists = new ArrayList[towns.length]; //1-indexed
        ArrayList temp = new ArrayList(2);
        temp.add(new Integer(towns[towns.length - 2].id));
        temp.add(new Integer(towns[1].id));
        lists[towns[0].id] = temp;

        for (int i = 1; i < towns.length - 1; i++) {
            ArrayList temp2 = new ArrayList(2);
            temp2.add(new Integer(towns[i - 1].id));
            temp2.add(new Integer(towns[i + 1].id));
            lists[towns[i].id] = temp2;
        }
        return lists;
    }

}
