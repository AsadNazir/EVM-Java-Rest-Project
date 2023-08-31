package com.example.evm_2.domain;

public class VoteCounts {
    String party;
    int votes;

    public String getParty() {
        return party;
    }

    public VoteCounts(String party, int votes) {
        this.party = party;
        this.votes = votes;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public VoteCounts() {
    }
}
