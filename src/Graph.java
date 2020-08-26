// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.io.Serializable;

public class Graph<T> implements Serializable {

    private HashedDictionaryOpenAddressingPerfectInstrumented<T, LList<T>> mainGraph;

    public Graph(){
        mainGraph = new HashedDictionaryOpenAddressingPerfectInstrumented<>();
    }

    public LList<T> getValue(T username) {
        return mainGraph.getValue(username);
    }

    public void addVertex(T username){
        LList<T> list = new LList<>();
        mainGraph.add(username, list);
    }

    public void removeVertex(T username){
        if (mainGraph.contains(username)){
            mainGraph.remove(username);
        }else{
            System.out.println("No such vertex exists.");
        }
    }

    public void addOneWayLink(T usernameFrom, T usernameTo){
        if (mainGraph.contains(usernameFrom)) {
            mainGraph.getValue(usernameFrom).add(usernameTo);
        } else {
            System.out.println("No such user exists in network.");
        }
    }

    public void removeLink(T usernameFrom, T usernameTo){
        if (mainGraph.contains(usernameFrom)) {
            LList<T> list = mainGraph.getValue(usernameFrom);
            if (list.contains(usernameTo)) {
                int position = list.findEntryPosition(usernameTo);
                list.remove(position);
            } else {
                System.out.println("Requested user does not exist in list.");
            }
        } else {
            System.out.println("No such user exists in network.");
        }
    }

    public boolean contains(T anEntry){
        boolean result = false;

        result = mainGraph.contains(anEntry);

        return result;
    }

    public boolean isEmpty(){
        return mainGraph.isEmpty();
    }

    public String toString() {
        return mainGraph.toString();
    }

}
