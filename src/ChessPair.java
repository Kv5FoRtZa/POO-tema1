public class ChessPair<K extends Comparable<K>, V> implements Comparable<ChessPair<K, V>>{
    private K key;
    private V value;
    public ChessPair(K cheie, V val){
        this.key = cheie;
        this.value = val;
    }

    public ChessPair() {

    }

    public void setKey(K key) {
        this.key = key;
    }
    public void setValue(V value) {
        this.value = value;
    }
    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
    @Override
    public String toString() {
        String st = "";
        st += this.key;
        st += this.value;
        return st;
    }
    public int compareTo(ChessPair<K,V> obj){
        return this.key.compareTo(obj.key);
    }
}
