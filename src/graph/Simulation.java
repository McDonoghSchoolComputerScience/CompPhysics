/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.Scanner;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class Simulation {
    public static void main(String[] args) {
        Graph g = new Graph();
        Push<Vector> hiF = new HiFilter (g);
        Scanner input = new Scanner(System.in);
        while (input.hasNextInt())
            hiF.push (new Vector (input.nextInt(), input.nextInt(), 0));
    }
}
