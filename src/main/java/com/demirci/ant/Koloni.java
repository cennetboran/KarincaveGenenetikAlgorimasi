/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demirci.ant;

import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author ulasd
 */
public class Koloni {

    public Karinca karincalar[] = null;
    public static Graphics grafik;
    public double uzaklikMatrisi[][] = null;
    public int karincaSayisi;
    public double c = 1.0;
    public double feromonIzi[][] = null;
    public double olasiliklar[] = null;
    public int sehirSayisi = 0;
    public int turIndisi = 0;
    private Random rassal = new Random();
    private double Q = 100;
    public int[] enIyiTur;
    public double enIyiTurUzunlugu;

    private class Karinca { 

        public int tur[] = new int[uzaklikMatrisi.length];
        public boolean ziyaretEdildiMi[] = new boolean[uzaklikMatrisi.length];

        public void sehirZiyaretEt(int sehir) {
            tur[turIndisi + 1] = sehir;
            ziyaretEdildiMi[sehir] = true;
        }

        public boolean ziyaretEdildiMi(int sehir) {
            return ziyaretEdildiMi[sehir];
        }

        public double turUzunlugu() {
            double uzunluk = uzaklikMatrisi[tur[sehirSayisi - 1]][tur[0]]; 
            for (int i = 0; i < sehirSayisi - 1; i++) {
                uzunluk = uzunluk + uzaklikMatrisi[tur[i]][tur[i + 1]];
            }
            return uzunluk;
        }

        public void sifirla() {
            for (int i = 0; i < sehirSayisi; i++) {
                ziyaretEdildiMi[i] = false;
            }
        }

    }

    public void koloniolustur(int sehir) {
        Hesaplama hsp = new Hesaplama();
        uzaklikMatrisi = hsp.uzaklikmatrisi(SCREEN.koordinatlar);
        this.sehirSayisi = sehir;
        double alfa = Double.parseDouble(SCREEN.jTextField3.getText());
        double beta = Double.parseDouble(SCREEN.jTextField4.getText());
        double rassallikFaktoru = Double.parseDouble(SCREEN.jTextField5.getText());
        double buharlasmaFaktoru = Double.parseDouble(SCREEN.jTextField6.getText());

        karincaSayisi = sehirSayisi;
        feromonIzi = new double[sehirSayisi][sehirSayisi];
        olasiliklar = new double[sehirSayisi];
        karincalar = new Karinca[karincaSayisi];
        for (int k = 0; k < karincaSayisi; k++) {
            karincalar[k] = new Karinca();
        }
        int iterasyon = Integer.parseInt(SCREEN.jTextField7.getText());
        hesapla(iterasyon, alfa, beta, rassallikFaktoru, buharlasmaFaktoru);
    }

    public void hesapla(int iterasyon, double alfa, double beta, double rassallikFaktoru, double buharlasmaFaktoru) {
        double oncekiEnIyiTurUzunlugu = Double.MAX_VALUE;
        for (int i = 0; i < sehirSayisi; i++) {
            for (int j = 0; j < sehirSayisi; j++) {
                feromonIzi[i][j] = c;
            }
        }
        for (int i = 0; i < iterasyon; i++) {

            karincaAyarla();
            karincaYurut(alfa, beta, rassallikFaktoru);
            feromonGuncelle(buharlasmaFaktoru);
            enIyiTurBelirle();
            SCREEN.jTextArea1.append(String.valueOf(i + 1) + ". iterasyon :\n ");
            for (int j = 0; j < enIyiTur.length; j++) {
                SCREEN.jTextArea1.append(Integer.toString(enIyiTur[j] + 1) + "-");
            }
            SCREEN.jTextArea1.append("\n");
            if (enIyiTurUzunlugu < oncekiEnIyiTurUzunlugu) {
                SCREEN.kenarCiz(grafik, enIyiTur);
                oncekiEnIyiTurUzunlugu = enIyiTurUzunlugu;
            }
            SCREEN.jTextArea1.append("\nEn iyi tur uzunluğu : " + Double.toString(enIyiTurUzunlugu - sehirSayisi));
            SCREEN.jTextArea1.append("\n");
            for (int l = 0; l < enIyiTur.length; l++) {
                SCREEN.jTextArea2.append(Integer.toString(enIyiTur[l] + 1) + "-");
            }
            SCREEN.jTextArea2.append("\n");
            SCREEN.jTextField8.setText(Double.toString(enIyiTurUzunlugu - sehirSayisi) + " birim");
        }

    }

    private void enIyiTurBelirle() {
        if (enIyiTur == null) {
            enIyiTur = karincalar[0].tur;
            enIyiTurUzunlugu = karincalar[0].turUzunlugu();
        }
        for (int i = 1; i < karincaSayisi; i++) {
            if (karincalar[i].turUzunlugu() < enIyiTurUzunlugu) {
                enIyiTurUzunlugu = karincalar[i].turUzunlugu();
                enIyiTur = karincalar[i].tur.clone();
            }
        }
    }

    private void karincaAyarla() {
        turIndisi = -1;
        for (int i = 0; i < karincaSayisi; i++) {
            karincalar[i].sifirla();
            karincalar[i].sehirZiyaretEt(rassal.nextInt(sehirSayisi));
        }
        turIndisi++;
    }

    private void karincaYurut(double alfa, double beta, double rassallikFaktoru) {
        while (turIndisi < (sehirSayisi - 1)) {
            for (int i = 0; i < karincaSayisi; i++) {
                karincalar[i].sehirZiyaretEt(siradakiSehriSec(karincalar[i], alfa, beta, rassallikFaktoru));
            }
            turIndisi++;
        }
    }

    private int siradakiSehriSec(Karinca karinca, double alfa, double beta, double rassallikFaktoru) {
        if (rassal.nextDouble() < rassallikFaktoru) {
            int r = rassal.nextInt(sehirSayisi - turIndisi);
            int j = -1;
            for (int i = 0; i < sehirSayisi; i++) {
                if (!karinca.ziyaretEdildiMi(i)) {
                    j++;
                }
                if (j == r) {
                    return i;
                }
            }
        }
        return olasilikHesapla(karinca, alfa, beta);
    }

    private int olasilikHesapla(Karinca karinca, double alfa, double beta) {
        int i = karinca.tur[turIndisi];
        int maksOlasilikIndisi = 0;
        double maksOlasilik = 0.0;
        double olasiliklarToplami = 0.0;
        for (int j = 0; j < sehirSayisi; j++) {
            if (!karinca.ziyaretEdildiMi(j)) {
                olasiliklarToplami += Math.pow(feromonIzi[i][j], alfa) * Math.pow(1.0 / uzaklikMatrisi[i][j], beta);
            }
        }

        for (int j = 0; j < sehirSayisi; j++) {
            if (karinca.ziyaretEdildiMi(j)) {
                olasiliklar[j] = 0.0;
            } else {
                double pay = Math.pow(feromonIzi[i][j], alfa) * Math.pow(1.0 / uzaklikMatrisi[i][j], beta);
                olasiliklar[j] = pay / olasiliklarToplami;
                if (olasiliklar[j] > maksOlasilik) {
                    maksOlasilik = olasiliklar[j];
                    maksOlasilikIndisi = j;
                }
            }
        }
        return maksOlasilikIndisi;
    }

    private void feromonGuncelle(double buharlasmaFaktoru) {
        for (int i = 0; i < sehirSayisi; i++) {
            for (int j = 0; j < sehirSayisi; j++) {
                feromonIzi[i][j] = feromonIzi[i][j] * buharlasmaFaktoru;
            }
        }

        for (int i = 0; i < karincaSayisi; i++) {
            double karincaKatkisi = Q / karincalar[i].turUzunlugu();
            for (int j = 0; j < sehirSayisi - 1; j++) {
                feromonIzi[karincalar[i].tur[j]][karincalar[i].tur[j + 1]] += karincaKatkisi;
            }
            feromonIzi[karincalar[i].tur[sehirSayisi - 1]][karincalar[i].tur[0]] += karincaKatkisi; 
        }
    }
}
