package org.example.lab3;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class UsersApp {
    public  ArrayList<User> loadUsers()
    {
        ArrayList<User> users = new ArrayList<>();

        try {
            File readFile = new File("Users.txt");
            Scanner Reader = new Scanner(readFile);
            while(Reader.hasNextLine()) {
                String line = Reader.nextLine();
                int subspaceIndex = line.indexOf(" ");
                if(subspaceIndex == -1) {System.out.println("Please enter a valid Email as username"); continue;} /*CHANGE!!!!!!!!!!!!!!*/
                String tempUserEmail = line.substring(0, subspaceIndex);
                String tempUserPassword = line.substring(subspaceIndex + 2); //two subspaces divides between the mail and the password
                try{
                    User newUser = new User(tempUserEmail, tempUserPassword);
                    users.add(newUser);
                }
                catch(IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (FileNotFoundException e) {throw new RuntimeException(e);}
        // sorting by usernames
        Collections.sort(users, (u1, u2) -> u1.getName().compareTo(u2.getName()));
        for(User u:users)
        {
            System.out.println(u);
        }

        return users;
    }
}
