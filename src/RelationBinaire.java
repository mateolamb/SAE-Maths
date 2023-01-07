import java.lang.*;

public class RelationBinaire {

    // attributs

    private int n;           // n > 0, E = {0,1,2, ..., n-1}
    private boolean[][] matAdj;  // matrice d'adjacence de R
    private int m;           // cardinal de R
    private EE[] tabSucc;    // tableau des ensembles de successeurs


    // constructeurs

    /**
     * pré-requis : nb > 0
     * action : construit la relation binaire vide dans l'ensemble {0,1,2, ..., nb-1}
     */
    public RelationBinaire(int nb) {
        this.n = nb;
        this.m = 0; //Ensemble vide donc cardinal à 0
        this.matAdj = new boolean[nb][nb]; //Initialise une matrice carrée (valeur par défault 'false')
        this.tabSucc = new EE[nb];
    }

    //______________________________________________


    /**
     * pré-requis : nb > 0 et 0 <= p <= 1
     * action : construit une relation binaire aléatoire dans l'ensemble {0,1,2, ..., nb-1}
     * à laquelle chaque couple a la probabilité p d'appartenir.
     * En particulier, construit la relation vide si p = 0 et la relation pleine si p = 1.
     * Indication : Math.random() retourne un réel de type double aléatoire de l'intervalle [0,1[
     */
    public RelationBinaire(int nb, double p) {
        this(nb); //Création d'une relation binaire vide dans l'ensemble 0 à nb-1

        if (p == 1) // probabilité à 100%
        {
            for (int i = 0; i < this.n; i++) {
                this.tabSucc[i] = new EE(this.n);
                for (int j = 0; j < this.n; j++) {
                    this.matAdj[i][j] = true;
                    this.tabSucc[i].ajoutPratique(j);
                    this.m++;
                }
            }
        } else if (p != 0) {
            for (int i = 0; i < this.n; i++) {
                this.tabSucc[i] = new EE(this.n);
                for (int j = 0; j < this.n; j++) {
                    double r = Math.random();
                    if (r <= p) //Test pour savoir si on remplit le tableau
                    {
                        this.tabSucc[i].ajoutPratique(j);
                        this.matAdj[i][j] = true;
                        this.m++;
                    }
                }
            }
        }
    }

    //______________________________________________


    /**
     * pré-requis : nb > 0 et 1 <= choix <= 3
     * action : construit la relation binaire dans l'ensemble {0,1,2, ..., nb-1}
     * '=' si egal a la valeur vrai et '<=' sinon
     */
    public RelationBinaire(int nb, boolean egal) {
        this(nb);
        for (int i = 0; i < nb; i++) {
            for (int j = 0; j < nb; j++) {
                if (egal && i == j) {
                    this.matAdj[i][j] = true;
                    this.tabSucc[i].ajoutPratique(i);
                    this.m++;
                } else if (i <= j && !egal) {
                    this.matAdj[i][j] = true;
                    this.tabSucc[i].ajoutPratique(j);
                    this.m++;
                }
            }
        }
    }

    //______________________________________________


    /**
     * pré-requis : mat est une matrice carrée de dimension > 0
     * action : construit une relation binaire dont la matrice d'adjacence
     * est une copie de mat
     */
    public RelationBinaire(boolean[][] mat) {
        this(mat.length);

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if (mat[i][j]) //Test pour savoir si il y a une liaison
                {
                    this.matAdj[i][j] = true;
                    this.tabSucc[i].ajoutPratique(j);
                    this.m++;
                }
            }
        }
    }

    //______________________________________________


    /**
     * pré-requis : tab.length > 0 et pour tout i, les éléments de tab[i]
     * sont compris entre 0 et tab.length-1
     * action : construit une relation binaire dont le tableau des ensembles de successeurs
     * est une copie de tab
     */
    public RelationBinaire(EE[] tab) {
        this(tab.length);

        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].getCardinal(); j++) {
                if (tab[i].getValue(j) == 0) //Test pour savoir si il y a une liaison
                {
                    this.tabSucc[i].ajoutPratique(tab[i].getValue(j));
                    this.matAdj[i][j] = true;
                    this.m++;
                }
            }
        }
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * action : construit une copie de r
     */
    public RelationBinaire(RelationBinaire r) {
        this(r.n);
        this.m = r.m;

        for (int i = 0; i < r.m; i++) {
            for (int j = 0; j < r.m; j++) {
                this.matAdj[i][j] = r.matAdj[i][j];
                this.tabSucc[i].ajoutPratique(r.tabSucc[i].getValue(j));
            }
        }
    }


    //______________________________________________


    // méthodes


    /**
     * pré-requis : aucun
     * résultat : une chaîne de caractères permettant d'afficher this par sa matrice d'adjacence
     * contenant des '0' et des '1' (plus lisibles que des 'V' et des 'F') et sa définition
     * en extension (ensemble de couples {(..,..),(..,..), ...})
     */
    public String toString() {
        String matrice = "";
        String couples = "";
        for (int i = 0; i < matAdj.length; i++) {
            for (int j = 0; j < matAdj.length; j++) {
                if (matAdj[i][j]) {
                    matrice += "1 ";
                    couples += "(" + i + "," + j + "),";
                }
            }
        }
        return matrice + "\n" + couples;
    }

    //______________________________________________


    // A) Logique et calcul matriciel
    //-------------------------------


    /**
     * pré-requis : m1 et m2 sont des matrices carrées de même dimension et 1 <= numConnecteur <= 5
     * résultat : la matrice obtenue en appliquant terme à terme le  connecteur de numéro numConnecteur
     * sur m1 si numConnecteur  = 3 (dans ce cas le paramètre m2 n'est pas utilisé),
     * et sur m1 et m2 dans cet ordre sinon, sachant que les connecteurs "ou","et","non",
     * "implique"et "equivalent" sont numérotés de 1 à 5 dans cet ordre
     */

    public static boolean[][] opBool(boolean[][] m1, boolean[][] m2, int numConnecteur) {
        boolean[][] res = new boolean[m1.length][m1.length];
        if (numConnecteur == 1) {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1.length; j++) {
                    if (m1[i][j] || m2[i][j]) {
                        res[i][j] = true;
                    }
                }
            }
        }
        if (numConnecteur == 2) {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1.length; j++) {
                    if (m1[i][j] && m2[i][j]) {
                        res[i][j] = true;
                    }
                }
            }
        }
        if (numConnecteur == 3) {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1.length; j++) {
                    if (m1[i][j]) {
                        res[i][j] = false;
                    } else {
                        res[i][j] = true;
                    }
                }
            }
        }
        if (numConnecteur == 4) {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1.length; j++) {
                    res[i][j] = true;
                    if (m1[i][j] && !m2[i][j]) {
                        res[i][j] = false;
                    }
                }
            }
        }
        if (numConnecteur == 5) {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1.length; j++) {
                    if ((m1[i][j] && m2[i][j]) || (!m1[i][j] && !m2[i][j])) {
                        res[i][j] = true;
                    }
                }
            }
        }
        return res;
    }

    //______________________________________________


    /**
     * pré-requis : m1 et m2 sont des matrices carrées de même dimension
     * résultat : le produit matriciel de m1 et m2
     */
    public static boolean[][] produit(boolean[][] m1, boolean[][] m2) {

        return opBool(m1, m2, 2);
    }

    //______________________________________________


    /**
     * pré-requis : m est une matrice carrée
     * résultat : la matrice transposée de m
     */
    public static boolean[][] transposee(boolean[][] m) {
        boolean[][] res = new boolean[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                res[j][i] = m[i][j];
            }
        }
        return res;
    }

    //______________________________________________


    // B) Théorie des ensembles
    //--------------------------


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est vide
     */
    public boolean estVide() {
        return this.m == 0;
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est pleinee (contient tous les couples d'éléments de E)
     */
    public boolean estPleine() {
        return this.n * this.n == this.m;
    }

    //______________________________________________

    /**
     * pré-requis : aucun
     * résultat : vrai ssi (x,y) appartient à this
     */
    public boolean appartient(int x, int y) {
        return matAdj[x][y];
    }

    //______________________________________________


    /**
     * pré-requis : 0 <= x < this.n et 0 <= y < this.n
     * résultat : ajoute (x,y) à this s'il n'y est pas déjà
     */
    public void ajouteCouple(int x, int y) {
        if (this.matAdj[x][y]) System.out.println("Le couple existe déjà");
        else {
            this.matAdj[x][y] = true;
            this.tabSucc[x].ajoutPratique(y);
            this.m++;
        }
    }

    //______________________________________________


    /**
     * pré-requis : 0 <= x < this.n et 0 <= y < this.n
     * résultat : enlève (x,y) de this s'il y est
     */
    public void enleveCouple(int x, int y) {
        if (!this.matAdj[x][y]) System.out.println("Couple inexistant");
        else {
            this.matAdj[x][y] = false;
            this.tabSucc[x].retraitElt(y);
        }
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : une nouvelle relation binaire obtenue à partir de this en ajoutant
     * les couples de la forme  (x,x) qui n'y sont pas déjà
     */
    public RelationBinaire avecBoucles() {
        RelationBinaire r = new RelationBinaire(this);
        for (int i = 0; i < r.matAdj.length; i++) {
            if (!r.matAdj[i][i]) {
                r.matAdj[i][i] = true;
                r.tabSucc[i].ajoutPratique(i);
                r.m++;
            }
        }
        return r;
    }
    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : une nouvelle relation binaire obtenue à partir de this en enlèvant
     * les couples de la forme  (x,x) qui y sont
     * //DERNIERE MODIF
     */
    public RelationBinaire sansBoucles() {
        boolean[][] b = new boolean[matAdj.length][matAdj.length];
        for (int i = 0; i < matAdj.length; i++) {
            if (matAdj[i][i]) {
                b[i][i] = false;
            }
        }
        return new RelationBinaire(b);
    }

    //______________________________________________


    /**
     * pré-requis : this.n = r.n
     * résultat : l'union de this et r
     */
    public RelationBinaire union(RelationBinaire r) {
        boolean[][] b = opBool(this.matAdj, r.matAdj, 1);

        return new RelationBinaire(b);
    }

    //______________________________________________


    /**
     * pré-requis : this.n = r.n
     * résultat : l'intersection de this et r
     */
    public RelationBinaire intersection(RelationBinaire r) {
        boolean[][] b = opBool(matAdj, r.matAdj, 2);
        return new RelationBinaire(b);
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : la relation complémentaire de this
     */
    public RelationBinaire complementaire() {
        boolean[][] b = opBool(matAdj, matAdj, 3);
        return new RelationBinaire(b);
    }

    //______________________________________________


    /**
     * pré-requis : this.n = r.n
     * résultat : la différence de this et r
     */
    public RelationBinaire difference(RelationBinaire r) {
        boolean[][] b = opBool(matAdj, r.matAdj, 4);
        return new RelationBinaire(b);
    }

    //______________________________________________


    /**
     * pré-requis : this.n = r.n
     * résultat : vrai ssi this est incluse dans r
     */
    public boolean estIncluse(RelationBinaire r) {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.matAdj[i][j] && this.matAdj[i][j] != r.matAdj[i][j]) return false;
            }
        }
        return true;
    }

    //______________________________________________


    /**
     * pré-requis : this.n = r.n
     * résultat : vrai ssi this est égale à r
     */
    public boolean estEgale(RelationBinaire r) {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.matAdj[i][j] != r.matAdj[i][j]) return false;
            }
        }
        return true;
    }

    //______________________________________________


    // C) Théorie des graphes orientés
    //---------------------------------

    /**
     * pré-requis : 0 <= x < this.n
     * résultat : l'ensemble des successeurs de x dans this, "indépendant"
     * (c'est-à-dire dans une autre zône mémoire) de l'attribut this.tabSucc
     */
    public EE succ(int x) {
        return new EE(this.tabSucc[x]);
    }

    //______________________________________________


    /**
     * pré-requis : 0 <= x < this.n
     * résultat : l'ensemble des prédécesseurs de x dans this
     */
    public EE pred(int x) {
        EE predecesseur = new EE(this.n);
        for (int i = 0; i < this.tabSucc.length; i++) {
            for (int j = 0; j < this.tabSucc[i].getCardinal(); j++) {
                if (this.tabSucc[i].getValue(j) == x) predecesseur.ajoutElt(i);
            }
        }
        return predecesseur;
    }

    //______________________________________________


    // D) Relation binaire
    //---------------------

    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est réflexive
     */
    public boolean estReflexive() {
        RelationBinaire rb = this.avecBoucles();
        return rb.m == this.m;
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est antiréflexive
     */
    public boolean estAntireflexive() {
        return !this.estReflexive();
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est symétrique
     */
    public boolean estSymetrique() {
        for (int i = 0; i < this.matAdj.length; i++) {
            for (int j = 0; j < this.matAdj[i].length; j++) {
                if (this.matAdj[i][j] && !this.matAdj[j][i]) return false;
            }
        }
        return true;
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est antisymétrique
     */
    public boolean estAntisymetrique() {
        return !this.estSymetrique();
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est transitive
     */
    public boolean estTransitive() {
        EE succ = new EE(this.n);
        EE succ2 = new EE(this.n);
        for (int i = 0; i < tabSucc.length-1; i++) {
            succ=this.succ(i);
            for (int j = 0; j < succ.getCardinal(); j++) {
                succ2=this.succ(succ.getValue(j));
                for (int k = 0; k < succ2.getCardinal(); k++) {
                    if(!succ.contient(succ.getValue(k))) return false;
                }
            }
        }
        return true;
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : vrai ssi this est une relation d'ordre
     */
    public boolean estRelOrdre() {
        return this.estReflexive() && this.estAntisymetrique() && this.estTransitive();
    }

    //______________________________________________


    /**
     * pré-requis : aucun
     * résultat : la relation binaire associée au diagramme de Hasse de this
     */
    public RelationBinaire hasse() {
        throw new RuntimeException("La fonction n'est pas encore implémentée !");
    }

    //______________________________________________

    /**
     * pré-requis : aucun
     * résultat : la fermeture transitive de this
     */
    public RelationBinaire ferTrans() {
        throw new RuntimeException("La fonction n'est pas encore implémentée !");
    }

    //______________________________________________

    /**
     * pré-requis : aucun
     * action : affiche this sous 2 formes (matrice et ensemble de couples), puis affiche ses propriétés
     * (réflexive, ..., relation d'ordre) et les relations binaires suivantes obtenues à partir de this :
     * Hasse, fermeture transitive de Hasse et fermeture transitive de Hasse avec boucles (sous 2 formes aussi)
     */
    public void afficheDivers() {
        int[][] copie = new int[this.n][this.n];
    }

    //______________________________________________

    public static void main(String[] args) {

        int nb;
        double p;
        do {
            Ut.afficher("\nDonner le cardinal de E (>0) : ");
            nb = Ut.saisirEntier();
        }
        while (nb <= 0);
    }
} // fin RelationBinaire