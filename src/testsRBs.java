//placer ce fichier dans le meme dossier que les *.java fournis par les étudiants, et lancer le main de ce fichier.

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.*;

public class testsRBs {

    public static void main(String[] args) {

        double note = 0;

        // Ici on lance les tests, si l'appels aux méthodes concernées déclenche une exception ou fait une boucle infinie, pas de pb, on passera qd même au suivant.

        note += runTest(testsRBs::testTransposee, "testTransposee",1); // Le dernier paramètre est le barême du test.
        note += runTest(testsRBs::testProduit, "testProduit",1);
        note += runTest(testsRBs::testOpBool, "testOpBool",1);
        note += runTest(testsRBs::testEstVide, "testEstVide",1);
        note += runTest(testsRBs::testAppartient, "testAppartient",1);
        note += runTest(testsRBs::testAjouteCouple, "testAjouteCouple",1);
        note += runTest(testsRBs::testAvecBoucles, "testAvecBoucles",1);
        note += runTest(testsRBs::testUnion, "testUnion",1);
        note += runTest(testsRBs::testIntersection, "testIntersection",1);
        note += runTest(testsRBs::testComplementaire, "testComplementaire",1);
        note += runTest(testsRBs::testEstIncluse, "testEstIncluse",1);
        note += runTest(testsRBs::testEstSymetrique, "testEstSymetrique",1);
        note += runTest(testsRBs::testEstTransitive, "testEstTransitive",1);
        note += runTest(testsRBs::testEstRelOrdre, "testEstRelOrdre",1);
        note += runTest(testsRBs::testFerTrans, "testFerTrans",1);

        System.out.println("fin des tests : note sur 15 = " + note);

    }


    public static double runTest(Callable<Double> r, String s, int bareme){
        ExecutorService executorService = Executors.newSingleThreadExecutor(); //si on submit à nouveau sans re créer ça timeout aussi pour tests suivants
        Future<Double> future = executorService.submit(r);
        double note = 0;
        try {

            note+= future.get(1L, TimeUnit.SECONDS)*bareme; //get renvoie entre 0 et 1
            System.out.println("****************************************************");
            System.out.println(s + " terminé, note: " + note + "/" + bareme);
            System.out.println("****************************************************");

        } catch (TimeoutException e) {
            System.out.println("****************************************************");
            System.out.println(s + " timeout");
            System.out.println("****************************************************");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("****************************************************");
            System.out.println(s + " erreur " + e.getMessage());
            System.out.println("****************************************************");
        }
        executorService.shutdownNow();
        return note;
    }


    private static double testOpBool() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m2= {
                {false,false,true},
                {false,true,true},
                {true,true,false}
        };
        boolean[][] m= {
                {true,false,true},
                {false,true,true},
                {true,true,false}
        };

        boolean[][] m3 = RelationBinaire.opBool(m1, m2, 1);

        if(Arrays.deepEquals(m,m3))
            return 1;
        else
            return 0;

    }

    private static double testProduit() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m2= {
                {false,false,true},
                {false,true,true},
                {true,true,false}
        };
        boolean[][] m= {
                {true,true,true},
                {true,true,false},
                {false,true,true}
        };

        boolean[][] res = RelationBinaire.produit(m1,m2);

        if (Arrays.deepEquals(m,res))
            return 1;
        else
            return 0;


    }


    private static double testTransposee() {
        boolean[][] m1= {
                {true,false,false},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m= {
                {true,false,true},
                {false,false,true},
                {false,true,false}
        };

        boolean[][] res = RelationBinaire.transposee(m1);

        if (Arrays.deepEquals(m,res))
            return 1;
        else
            return 0;
    }


    private static double testEstVide() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        if(RB.estVide())
            return 0;
        else
            return 1;
    }



    private static double testAppartient() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        if(RB.appartient(0,0) && !RB.appartient(2,2))
            return 1;
        else
            return 0;


    }


    private static double testAjouteCouple() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RB.ajouteCouple(0,1);
        if(RB.appartient(0,1))
            return 1;
        else
            return 0;
    }




    private static double testAvecBoucles() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = RB.avecBoucles();
        for(int i = 0; i < 3; i++)
            if(!RB2.appartient(i,i))
                return 0;
        return 1;
    }


    private static double testUnion() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m2= {
                {true,true,false},
                {true,true,false},
                {true,true,false}
        };
        boolean[][] m= {
                {true,true,true},
                {true,true,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = new RelationBinaire(m2);
        RelationBinaire RB3 = RB.union(RB2);
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(RB3.appartient(i,j) != (RB.appartient(i,j) | RB2.appartient(i,j)))
                    return 0;
        return 1;
    }


    private static double testIntersection() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m2= {
                {true,true,false},
                {true,true,false},
                {true,true,false}
        };
        boolean[][] m= {
                {true,false,false},
                {false,false,false},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = new RelationBinaire(m2);
        RelationBinaire RB3 = RB.intersection(RB2);
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(RB3.appartient(i,j) != m[i][j])
                    return 0;
        return 1;
    }




    private static double testComplementaire() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m= {
                {false,true,false},
                {true,true,false},
                {false,false,true}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = RB.complementaire();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(RB2.appartient(i,j) != m[i][j])
                    return 0;
        return 1;
    }


    private static double testEstIncluse() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };
        boolean[][] m= {
                {true,false,true},
                {false,false,true},
                {true,false,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = new RelationBinaire(m);
        if(RB2.estIncluse(RB))
            return 1;
        else
            return 0;
    }

    private static double testEstSymetrique() {
        boolean[][] m1= {
                {true,false,true},
                {false,false,true},
                {true,true,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        if(RB.estSymetrique())
            return 1;
        else return 0;
    }


    private static double testEstTransitive() {
        boolean[][] m1= {
                {true,true,true},
                {false,false,true},
                {false,false,false}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        if(RB.estTransitive())
            return 1;
        else return 0;
    }

    private static double testEstRelOrdre() {
        boolean[][] m1= {
                {true,true,true},
                {false,true,true},
                {false,false,true}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        if(RB.estRelOrdre())
            return 1;
        else return 0;
    }

    private static double testFerTrans() {
        boolean[][] m1= {
                {true,true,false},
                {false,true,true},
                {false,false,true}
        };
        boolean[][] m2= {
                {true,true,true},
                {false,true,true},
                {false,false,true}
        };

        RelationBinaire RB = new RelationBinaire(m1);
        RelationBinaire RB2 = RB.ferTrans();
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(RB2.appartient(i,j) != m2[i][j])
                    return 0;
        return 1;
    }

} // end class