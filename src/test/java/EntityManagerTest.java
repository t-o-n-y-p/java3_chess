import org.java3.chess.model.Challenge;
import org.java3.chess.model.Game;
import org.java3.chess.model.Player;
import org.java3.chess.tools.Color;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static java.lang.Thread.sleep;

public class EntityManagerTest {

    private EntityManagerFactory factory;
    private EntityManager manager;

    @Test
    public void smokeTest() throws InterruptedException {
        factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();
            Player player1 = new Player("test" + System.currentTimeMillis(), "aaa");
            manager.persist(player1);
            manager.getTransaction().commit();
            sleep(10);
            manager.getTransaction().begin();
            Player player2 = new Player("test" + System.currentTimeMillis(), "aaa");
            manager.persist(player2);
            manager.getTransaction().commit();
            manager.getTransaction().begin();
            Game game = new Game(player1, player2);
            manager.persist(game);
            manager.getTransaction().commit();
            manager.getTransaction().begin();
            Challenge challenge = new Challenge(player1, player2, Color.WHITE);
            manager.persist(challenge);
            manager.getTransaction().commit();
        } finally {
            manager.close();
            factory.close();
        }
    }

}
