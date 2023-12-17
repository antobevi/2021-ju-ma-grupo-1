import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

// Porque AbstractPersistenceTest usa otra version de JUnit y no hace begin ni rollback.
public abstract class AbstractPersistanceUpdatedTest implements TransactionalOps, EntityManagerOps {

  public AbstractPersistanceUpdatedTest() {

  }

  @BeforeEach
  public void setUp() {
    this.beginTransaction();
  }

  @AfterEach
  public void rollback() {
    this.rollbackTransaction();
  }

}
