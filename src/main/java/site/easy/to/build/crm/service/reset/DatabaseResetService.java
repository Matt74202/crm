package site.easy.to.build.crm.service.reset;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DatabaseResetService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetDatabase() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); 

        entityManager.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE oauth_users").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_profile").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_roles").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE employee").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE email_template").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE customer_login_info").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE customer").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE trigger_lead").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE trigger_ticket").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE trigger_contract").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE contract_settings").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE lead_action").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE lead_settings").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE ticket_settings").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE file").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE google_drive_file").executeUpdate();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
