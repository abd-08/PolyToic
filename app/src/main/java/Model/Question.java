package Model;

public class Question {
    private boolean a,b,c,d;
    private int numero;
    private boolean isCorrect;
    private String bonneReponse;

    public Question(boolean a, boolean b, boolean c, boolean d,int numero) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.numero=numero;
        this.isCorrect = false;
        this.bonneReponse="0";
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Question(int numero) {
        this.a = false;
        this.b = false;
        this.c = false;
        this.d = false;
        this.numero=numero;
        this.isCorrect = false;
        this.bonneReponse="0";

    }

    public boolean isA() {
        return a;
    }

    public void setA(boolean a) {

        this.deselectAll();
        this.a = a;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.deselectAll();
        this.b = b;
    }

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.deselectAll();
        this.c = c;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.deselectAll();
        this.d = d;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void deselectAll(){
        this.a = false;
        this.b = false;
        this.c = false;
        this.d = false;
    }

    public boolean isSelect(){
        return this.a||this.b||this.c||this.d;
    }

    public String whoIs(){
        if (isA()) return "a";
        if (isB()) return "b";
        if (isC()) return "c";
        if (isD()) return "d";
        else return "0";
    }

    public void selectReponse(String ch){
        switch (ch){
            case "a":
                setA(true);break;
            case "b":
                setB(true);break;
            case "c":
                setC(true);break;
            case "d":
                setD(true);break;
            case "0":
                deselectAll();break;
        }
    }

    public String getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) { this.bonneReponse = bonneReponse; }

    public  boolean isEqual(Question question){
        boolean res = this.whoIs()==question.whoIs();
        if ( res) this.bonneReponse = question.whoIs();
        else  this.bonneReponse = this.whoIs();
        return res;
    }
}
