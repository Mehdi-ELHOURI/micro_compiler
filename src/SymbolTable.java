import java.util.Hashtable;

public class SymbolTable {

    //table
    private Hashtable<String,Entry> table;

    //taille
    private int size;

    public SymbolTable() {
        table = new Hashtable<>();
    }

    //ajout d'une entree
    public void addEntry(Entry newEntry){
        newEntry.setAddress(size);
        table.put(newEntry.getName(),newEntry);
        size+=newEntry.getSize();
    }

    //recuperer une entree
    public Entry getEntry(String entryId){
        if (this.hasEntry(entryId))
            return table.get(entryId);
        else return null;
    }

    //entree existe ?
    public boolean hasEntry(String entryId){
        return table.containsKey(entryId);
    }

    //toString

    @Override
    public String toString() {
        return "SymbolTable{" +
                "table=" + table +
                ", size=" + size +
                '}';
    }
}
