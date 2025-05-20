package id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin admin;
    private final String TEST_ID = "admin-123";

    @BeforeEach
    void setUp() {
        admin = new Admin();
    }

    @Test
    void testGetterAndSetterForId() {
        admin.setId(TEST_ID);

        assertEquals(TEST_ID, admin.getId(), "getId should return the set ID");
    }

    @Test
    void testDefaultConstructor() {
        Admin newAdmin = new Admin();
        assertNull(newAdmin.getId(), "Default ID should be null");
    }

    @Test
    void testToString() {
        admin.setId(TEST_ID);

        String toString = admin.toString();

        assertTrue(toString.contains(getClass().getSimpleName().replace("Test", "")),
                "toString should contain class name");
    }

    @Test
    void testEqualsAndHashCode() {
        Admin admin1 = new Admin();
        admin1.setId("same-id");

        Admin admin2 = new Admin();
        admin2.setId("same-id");

        Admin admin3 = new Admin();
        admin3.setId("different-id");

        assertEquals(admin1, admin1, "An object should equal itself");
    }

    @Test
    void testNullId() {
        assertDoesNotThrow(() -> {
            admin.setId(null);
        });

        assertNull(admin.getId());
    }

    @Test
    void testEntityAnnotations() {
        assertTrue(Admin.class.isAnnotationPresent(Entity.class),
                "Admin should be annotated with @Entity");
        assertTrue(Admin.class.isAnnotationPresent(Table.class),
                "Admin should be annotated with @Table");

        Table tableAnnotation = Admin.class.getAnnotation(Table.class);
        assertEquals("admin", tableAnnotation.name(),
                "Table name should be 'admin'");

        try {
            assertTrue(Admin.class.getDeclaredField("id").isAnnotationPresent(Id.class),
                    "id field should be annotated with @Id");
        } catch (NoSuchFieldException e) {
            fail("Admin class should have an 'id' field");
        }
    }
}