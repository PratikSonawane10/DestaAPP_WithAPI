package com.choicespropertysolutions.desta.Singleton;

import java.util.ArrayList;

public class UserVoteList {

    private static ArrayList<String> userVoteListId = new ArrayList<String>();

    public static ArrayList<String> getVoteList() {
        return userVoteListId;
    }
    public static void setVoteList(ArrayList<String> voteId) {
        userVoteListId = voteId;
    }
}
