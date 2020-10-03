package Model;

public class Mode {
    public static final int EXAMEN=0;
    public static final int ENTRAINEMENT=1;
    public static final int TERMINEE=2;
    public static final int AUTRE=3;
    private int THE_MODE;
    private  boolean running ;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Mode(int mode){
        switch (mode){
            case EXAMEN:
                THE_MODE=EXAMEN;break;
            case ENTRAINEMENT:
                THE_MODE=ENTRAINEMENT;break;
            case TERMINEE:
                THE_MODE=TERMINEE;break;
            default:
                THE_MODE=AUTRE;
                break;
        }
    }

    public int getTHE_MODE(){
        return  this.THE_MODE;
    }

    public String getMODE(){
        switch (this.THE_MODE){
            case EXAMEN:
                return "examen";
            case ENTRAINEMENT:
                return "entrainement";
            case TERMINEE:
                return "termine";
            default:
                return "autre";
        }
    }

    public static String getMODE(int mode){
        switch (mode){
            case EXAMEN:
                return "examen";
            case ENTRAINEMENT:
                return "entrainement";
            case TERMINEE:
                return "termine";
            default:
                return "autre";
        }
    }

    public boolean equalMode(Mode mode){
        return this.THE_MODE==mode.getTHE_MODE();
    }

    public boolean equalMode(int mode){
        return this.THE_MODE==mode;
    }


}

