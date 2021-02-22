import org.java3.chess.model.Challenge;
import org.java3.chess.model.Game;
import org.java3.chess.model.Move;
import org.java3.chess.model.User;
import org.java3.chess.model.Color;
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
            User player1 = new User("test" + System.currentTimeMillis(), "aaa");
            manager.persist(player1);
            manager.getTransaction().commit();
            sleep(10);
            manager.getTransaction().begin();
            User player2 = new User("test" + System.currentTimeMillis(), "aaa");
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
            manager.getTransaction().begin();
            Move move = new Move(game, "e2e4");
            manager.persist(move);
            manager.getTransaction().commit();
        } finally {
            manager.close();
            factory.close();
        }
    }

}
