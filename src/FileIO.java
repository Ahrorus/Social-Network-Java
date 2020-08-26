// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.io.*;
import java.util.LinkedList;

public final class FileIO {

    public static void saveProfileMap(String fileName, HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile> profileMap) {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(profileMap);
            out.close();
            file.close();
        } catch(IOException e) {
            System.out.println("\nError saving the profileMap to the database.");
            e.printStackTrace();
        }
    }

    public static void saveNameToUsernameMap(String fileName, HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>> nameToUsername) {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(nameToUsername);
            out.close();
            file.close();
        } catch(IOException e) {
            System.out.println("\nError saving the nameToUsername to the database.");
            e.printStackTrace();
        }
    }

    public static void saveFriendsListMap(String fileName, Graph<String> friendsList) {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(friendsList);
            out.close();
            file.close();
        } catch(IOException e) {
            System.out.println("\nError saving the friendsList to the database.");
            e.printStackTrace();
        }
    }

    public static HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile> loadProfileMap(String fileName) {
        HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile> profileMap = null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            profileMap = (HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile>) in.readObject();
            in.close();
            file.close();
            return profileMap;
        } catch(Exception e) {
            System.out.println("\nError loading the profileMap from the database.");
            e.printStackTrace();
        }
        return profileMap;
    }

    public static HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>> loadNameToUsernameMap(String fileName) {
        HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>> nameToUsernameMap = null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            nameToUsernameMap = (HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>>) in.readObject();
            in.close();
            file.close();
            return nameToUsernameMap;
        } catch(Exception e) {
            System.out.println("\nError loading the nameToUsernameMap from the database.");
            e.printStackTrace();
        }
        return nameToUsernameMap;
    }

    public static Graph<String> loadFriendsListMap(String fileName) {
        Graph<String> friendsListMap = null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            friendsListMap = (Graph<String>) in.readObject();
            in.close();
            file.close();
            return friendsListMap;
        } catch(Exception e) {
            System.out.println("\nError loading the friendsListMap from the database.");
            e.printStackTrace();
        }
        return friendsListMap;
    }

}
