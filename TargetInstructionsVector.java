import java.util.ArrayList;

public class TargetInstructionsVector {

    private ArrayList<TargetInstruction> tiv;

    public TargetInstructionsVector() {
        tiv = new ArrayList<>();
    }

    public void addTInstruction(TargetInstruction ti){
        tiv.add(ti);
    }

    public int size(){
        return tiv.size();
    }

    public TargetInstruction getTInstruction(int index){
        return tiv.get(index);
    }

    @Override
    public String toString() {
        String str = "";
//        for (TargetInstruction ti:tiv)
//            str += ti + "\n";
        for (int i = 0; i < tiv.size(); i++) {
            str+= i + ": " + tiv.get(i) + "\n";
        }
        return str;
    }
}
