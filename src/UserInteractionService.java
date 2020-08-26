// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.util.Iterator;
import java.util.Scanner;

public class UserInteractionService {
    ProfileService profileService;

    public UserInteractionService(){
        this.profileService = new ProfileService();
    }

    public void createProfile(){
        String username;
        boolean control = true;
        do {
            username = UserInteractionService.getString("Please enter your desired username: ");
            if (!profileService.getProfileMap().contains(username)) {
                control = false;
            }else System.out.println("Username is taken. Try again.");
        } while (control);

        String password = UserInteractionService.getString("Please enter your desired password: ");
        String name = UserInteractionService.getString("Please enter your desired name: ");

        profileService.createProfile(new Profile(username, password, name));
    }

    public void deleteProfile(String username){
        if (profileService.getProfileMap().contains(username)) {
            profileService.deleteProfile(username);
        } else System.out.println("No such profile exists.");
    }

    public void changeName(String username){
        String newName = getString("Enter new name: ");
        profileService.changeName(username, newName);
    }

    public void changePassword(String username){
        String newPassword = getString("Enter new password: ");
        profileService.changePassword(username,newPassword);
    }

    public void changeStatus(String username){
        String newStatus = getString("Enter :\n\tOffline\n\tOnline\n\tBusy");
        profileService.changeStatus(username, newStatus);
    }

    public Profile searchProfiles(){
        String profileName = getString("Enter the name of profile you wish to search for: ");
        String profileUsername = findFriend(profileName);
        return profileService.getProfileMap().getValue(profileUsername);
    }

    public String findFriend(String name){
        if (profileService.getNameToUsername().contains(name)){
            return profileService.findFriend(name);
        }else {
            return null;
        }
    }

    public void addFriend(String you){
        String newFriendName = getString("Enter the name of your friend: ");
        String newFriend = findFriend(newFriendName);
        if (!profileService.getFriendListMap().getValue(you).contains(newFriend)) {
            profileService.addFriend(you, newFriend);
        }else System.out.println("Cannot add friend, friend is already your friend.");
    }

    public void removeFriend(String you){
        String oldFriendName = getString("Enter the name of your friend: ");
        String oldFriend = findFriend(oldFriendName);
        if (profileService.getFriendListMap().getValue(you).contains(oldFriend)) {
            profileService.removeFriend(you, oldFriend);
        }else System.out.println("Cannot remove friend, friend is already not your friend.");
    }

    public boolean modifyProfile(String username){
        while (true) {
            try {
                switch (UserInteractionService.getString("Enter one of the following options:\n\tChange Name\n\tChange Password\n\tSet Status\n\tSearch Profiles\n\tFind Mutual Friends\n\tAdd Friend\n\tRemove Friend\n\tDelete My Profile\n\tLog Out\n")){
                    case "Change Name":
                        changeName(username);
                        return true;
                    case "Change Password":
                        changePassword(username);
                        return true;
                    case "Set Status":
                        changeStatus(username);
                        return true;
                    case "Search Profiles":
                        Profile profile = searchProfiles();
                        String username2 = profile.getUsername();
                        System.out.println(profile);
                        System.out.println("Friends :" + profileService.getFriendListMap().getValue(username2).toString());
                        return true;
                    case "Find Mutual Friends":
                        LinkedDictionary<String, Integer> mutuals = profileService.getMutualFriendsList(username);
                        Iterator<String> key = mutuals.getKeyIterator();
                        Iterator<Integer> number = mutuals.getValueIterator();
                        int size = mutuals.getSize();

                        System.out.println("Mutual Friends:");
                        for (int i = 1; i <= size; i++) {
                            System.out.println(profileService.getProfileMap().getValue(key.next()).getName() + " has " + number.next() + " mutual friends with you.");
                        }
                        return true;
                    case "Add Friend":
                        addFriend(username);
                        return true;
                    case "Remove Friend":
                        removeFriend(username);
                        return true;
                    case "Delete My Profile":
                        deleteProfile(username);
                        return false;
                    case "Log Out":
                        logOut(username);
                        return false;
                    default:
                        System.out.println("Cannot recognize command.");
                }
            } catch (NullPointerException e) {
                System.out.println("No profile found.");
            }
        }
    }

    public static String getString(String message) {
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println();
            System.out.print(message);

            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                if (input != null)
                    return input;
            }
            System.out.println("An error has occurred, try again.");
        }
    }

    public void mainMenu(){
        // Load database
        profileService.loadDatabase();

        System.out.println("Welcome to AceBook");
        System.out.println("There are already some profiles stored in the hashedDictionaries that were loaded from serialized files.");
        System.out.println("Once you Exit, the modified hashedDictionaries will be displayed and reserialized (saved).");

        boolean control = true;
        while(control){
            switch (getString("Enter Login, Sign Up, or Exit\n")){
                case "Login":
                    logIn();
                    control = true;
                    break;
                case "Sign Up":
                    createProfile();
                    control = true;
                    break;
                case "Exit":
                    control = false;
                    profileService.saveDatabase(); // save database
                    profileService.showEverything(); // show everything
                    break;
                default:
                    System.out.println("Cannot recognize command.");
                    control = true;
            }
        }
    }

    public void logOut(String username){
        profileService.changeStatus(username, "Offline");
    }

    public void logIn(){
        String username = getString("Enter your username: ");
        String password = getString("Enter your password: ");

        if (profileService.getProfileMap().contains(username)) {
            boolean matchingPassword = password.equals(profileService.getProfileMap().getValue(username).getPassword());
            if (matchingPassword) {
                while(modifyProfile(username));
            }else{
                System.out.println("Incorrect username or password.");
            }
        } else System.out.println("Incorrect username or password.");
    }

}
