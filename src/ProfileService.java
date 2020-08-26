// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.util.LinkedList;

public class ProfileService {
    private HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile> profileMap;
    private HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>> nameToUsername;
    private Graph<String> friendListMap;

    public boolean isInitialized = false;
    // File paths
    private static final String PROFILE_MAP_PATH = "data\\profileMap.ser";
    private static final String NAME_TO_USERNAME_PATH = "data\\nameToUsername.ser";
    private static final String FRIENDS_LIST_MAP_PATH = "data\\friendsListMap.ser";

    public HashedDictionaryOpenAddressingPerfectInstrumented<String, Profile> getProfileMap() {
        return profileMap;
    }

    public HashedDictionaryOpenAddressingPerfectInstrumented<String, LList<String>> getNameToUsername() {
        return nameToUsername;
    }

    public Graph<String> getFriendListMap() {
        return friendListMap;
    }

    private void initialize(){
        profileMap = new HashedDictionaryOpenAddressingPerfectInstrumented<>();
        nameToUsername = new HashedDictionaryOpenAddressingPerfectInstrumented<>();
        friendListMap = new Graph<>();
        isInitialized = true;
    }

    public ProfileService(){
        initialize();
    }

    public void createProfile(Profile newProfile){
        profileMap.add(newProfile.getUsername(), newProfile);

        if (nameToUsername.contains(newProfile.getName())){
            nameToUsername.getValue(newProfile.getName()).add(newProfile.getUsername());
        }else {
            LList<String> usernameList = new LList<>();
            usernameList.add(newProfile.getUsername());
            nameToUsername.add(newProfile.getName(), usernameList);
        }

        friendListMap.addVertex(newProfile.getUsername());
    }

    public void deleteProfile(String username){
        Profile deletedProfile = profileMap.getValue(username);
        String name = deletedProfile.getName();

        LList<String> xList = nameToUsername.getValue(name);

        if (!xList.isEmpty()){
            if (xList.getLength() > 1){
                xList.remove(xList.findEntryPosition(username));
            }else{
                nameToUsername.remove(name);
            }
        }else{
            nameToUsername.remove(name);
        }

        profileMap.remove(username);
        friendListMap.removeVertex(username);
    }


    public void changeName(String username, String newName){
        profileMap.getValue(username).setName(newName);
    }

    public void changePassword(String username, String newPassword){
        profileMap.getValue(username).setPassword(newPassword);
    }

    public void changeStatus(String username, String newStatus){
        profileMap.getValue(username).setStatus(newStatus);
    }

    public String findFriend(String name){
        String username = null;

        LList<String> xList = nameToUsername.getValue(name);

        if (xList.getLength() == 1){
            return xList.getEntry(1);
        }else {
            boolean found = false;
            for (int i = 0; (i < xList.getLength()) && !found; i++) {
                System.out.println(profileMap.getValue(xList.getEntry(i)));
                if (UserInteractionService.getString("Enter 'Yes' if this is correct friend: ").equals("Yes")) {
                    found = true;
                    username = xList.getEntry(i);
                }
            }
            if (found) return username;
            else {
                System.out.println("Error.");
                return null;
            }
        }
    }

    public LinkedDictionary<String, Integer> getMutualFriendsList(String you){
        LinkedQueue<String> todo = new LinkedQueue<>();
        LinkedQueue<String> mutualList = new LinkedQueue<>();

        LList<String> list = friendListMap.getValue(you);

        for (int i = 1; i <= list.getLength(); i++) {
            todo.enqueue(list.getEntry(i));
        }

        LList<String> myList = friendListMap.getValue(you);

        while (!todo.isEmpty()){
            String currentFriend = todo.dequeue();

            LList<String> theirList = friendListMap.getValue(currentFriend);
            for (int i = 1; i <= theirList.getLength(); i++) {
                String entry = theirList.getEntry(i);
                if (!myList.contains(entry)){
                    mutualList.enqueue(entry);
                }
            }
        }

        LinkedDictionary<String, Integer> result = new LinkedDictionary<>();

        while(!mutualList.isEmpty()){
            String x = mutualList.dequeue();
            result.add(x, findNumberOfMutuals(you, x));
        }

        if (result.isEmpty())
            System.out.println("You have no mutual friends.");

        return result;

    }

    private Integer findNumberOfMutuals(String you, String them){
        Integer count = 0;

        LList<String> myList = friendListMap.getValue(you);
        LList<String> themList = friendListMap.getValue(them);

        for (int i = 1; i <= themList.getLength(); i++) {
            if (myList.contains(themList.getEntry(i)))
                count++;
        }

        return count;
    }

    public void addFriend(String you, String newFriend){
        friendListMap.getValue(you).add(newFriend);
    }

    public void removeFriend(String you, String oldFriend){
        LList<String> list = friendListMap.getValue(you);
        list.remove(list.findEntryPosition(oldFriend));
    }

    // Database code:
    public void saveDatabase() {
        FileIO.saveProfileMap(PROFILE_MAP_PATH, profileMap);
        FileIO.saveNameToUsernameMap(NAME_TO_USERNAME_PATH, nameToUsername);
        FileIO.saveFriendsListMap(FRIENDS_LIST_MAP_PATH, friendListMap);
    }

    public void loadDatabase() {
        profileMap = FileIO.loadProfileMap(PROFILE_MAP_PATH);
        nameToUsername = FileIO.loadNameToUsernameMap(NAME_TO_USERNAME_PATH);
        friendListMap = FileIO.loadFriendsListMap(FRIENDS_LIST_MAP_PATH);
    }

    public void showEverything() {
        System.out.println("The profileMap: ");
        System.out.println(profileMap);
        System.out.println("The nameToUsername Map: ");
        System.out.println(nameToUsername);
        System.out.println("The friendsListMap: ");
        System.out.println(friendListMap);
    }

}
