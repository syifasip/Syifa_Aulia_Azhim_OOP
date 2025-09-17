import Model.Player;
import Model.Score;
import Repository.PlayerRepository;
import Repository.ScoreRepository;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        PlayerRepository playerRepo = new PlayerRepository();
        ScoreRepository scoreRepo = new ScoreRepository()

        Player player1 = new Player("Stelle");
        Player player2 = new Player("gamerLooxmaxxing");
        Player player3 = new Player ("Stelle123");
        Player player4 = new Player ("Banananana");

        player1.updateHighScore(1500);
        player1.addCoins(250);
        player1.addDistance(5000);

        player2.updateHighScore(3200);
        player2.addCoins(750);
        player2.addDistance(12000);


        Model.Player player1 = new Model.Player("Nenek pargoy");
        Model.Player player2 = new Model.Player("Kakek backflip");

        Model.Score score1 = new Model.Score(player1.getPlayerId(), 1500, 250, 5000);
        Model.Score score2 = new Model.Score(player2.getPlayerId(), 3200, 750, 12000);
        Model.Score score3 = new Model.Score(player1.getPlayerId(), 1800, 300, 6000);

        player1.updateHighScore(score3.getValue());
        player1.addCoins(score1.getCoinsCollected());
        player1.addCoins(score3.getCoinsCollected());
        player1.addDistance(score1.getDistance());
        player1.addDistance(score3.getDistance());

        player2.updateHighScore(score2.getValue());
        player2.addCoins(score2.getCoinsCollected());
        player2.addDistance(score2.getDistance());

        System.out.println("========================== Player Details ==========================");
        player1.showDetail();
        player2.showDetail();

        score1.showDetail();
    }
}