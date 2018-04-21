package com.vamsi.java;

public class Main {

    public static void main(String[] args) {

        FootballPlayer joe = new FootballPlayer("Joe");
        BaseballPlayer pat = new BaseballPlayer("Pat");
        SoccerPlayer beckham = new SoccerPlayer("Beckham");

        Team<FootballPlayer> adelaideCrows = new Team<>("Adelaide Crows");
        adelaideCrows.addPlayer(joe);

        Team<BaseballPlayer> baseballTeam = new Team<>("Chicago Cubs");
        baseballTeam.addPlayer(pat);

        Team<SoccerPlayer> soccerPlayer = new Team<>("Adelaide Crows");
        soccerPlayer.addPlayer(beckham);

        System.out.println(adelaideCrows.numPlayers());

        Team<FootballPlayer> melbourne = new Team<>("Melbourne");
        FootballPlayer banks = new FootballPlayer("Gordon");
        melbourne.addPlayer(banks);

        Team<FootballPlayer> hawThorn = new Team<>("HawThorn");
        Team<FootballPlayer> fremantle = new Team<>("Fremantle");

        hawThorn.matchResult(hawThorn,1,0);
        fremantle.matchResult(adelaideCrows,3,8);

        adelaideCrows.matchResult(fremantle, 2,3);

        System.out.println("Rankings");
        System.out.println(adelaideCrows.getName() + ": " + adelaideCrows.ranking());
        System.out.println(melbourne.getName() + ": " + melbourne.ranking());
        System.out.println(fremantle.getName() + ": " + fremantle.ranking());
        System.out.println(hawThorn.getName() + ": " + hawThorn.ranking());

        System.out.println(adelaideCrows.compareTo(melbourne));
        System.out.println(adelaideCrows.compareTo(hawThorn));
        System.out.println(melbourne.compareTo(fremantle));

    }
}
