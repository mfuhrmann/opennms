/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.test.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.opennms.bootstrap.Bootstrap;
import org.opennms.core.db.install.SimpleDataSource;
import org.opennms.core.schema.ExistingResourceAccessor;
import org.opennms.core.schema.Migration;
import org.opennms.core.schema.Migrator;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Note that this test may have some issues inside Eclipse related to
 * Spring's ability to classload the changelog.xml files by using the
 * "classpath*:/changelog.xml" resource identifier. Run it outside Eclipse
 * using mvn if you run into problems.
 */
//@RunWith(OpenNMSJUnit4ClassRunner.class)
//@ContextConfiguration(locations={
//        "classpath:/migratorTest.xml"
//})
//@JUnitTemporaryDatabase
public class DatabasePopulatorIT {
    private static final Logger LOG = LoggerFactory.getLogger(DatabasePopulatorIT.class);

    @Autowired
    DataSource m_dataSource;

    @Autowired
    ResourceLoader m_resourceLoader;

    @Autowired
    GenericApplicationContext m_context;

    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();
        
        //m_context = new StaticApplicationContext();
        m_context = new GenericApplicationContext();
        m_context.setClassLoader(Bootstrap.loadClasses(new File(System.getProperty("opennms.home", "../../../../opennms/opennms-base-assembly/src/main/filtered")), true));
        
        m_resourceLoader = new DefaultResourceLoader(m_context.getClassLoader());
    }

//    @Test
//    @JUnitTemporaryDatabase(createSchema=false)
//    public void testUpdate() throws Exception {
//        // Make sure there is no databasechangelog table
//        assertFalse(changelogExists());
//
//        Resource aResource = null;
//        for (final Resource resource : getTestResources()) {
//            URI uri = resource.getURI();
//            if (uri.getScheme().equals("file") && uri.toString().contains("test-api/schema/a")) {
//                aResource = resource;
//            }
//            if (uri.getScheme().equals("jar") && uri.toString().contains("test-api.schema.a")) {
//                aResource = resource;
//            }
//        }
//        assertNotNull("aResource must not be null", aResource);
//
//        Set<String> tables = getTables();
//        assertFalse("must not contain table 'schematest'", tables.contains("schematest"));
//
//        final Migration migration = new Migration();
//        migration.setAdminUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setAdminPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setDatabaseUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setDatabasePassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setChangeLog("changelog.xml");
//        migration.setAccessor(new ExistingResourceAccessor(aResource));
//
//        LOG.info("Running migration on database: {}", migration);
//
//        final Migrator m = new Migrator();
//        m.setDataSource(m_dataSource);
//        m.setAdminDataSource(m_dataSource);
//        m.setValidateDatabaseVersion(false);
//        m.setCreateUser(false);
//        m.setCreateDatabase(false);
//
//        m.prepareDatabase(migration);
//        m.migrate(migration);
//
//        LOG.info("Migration complete: {}", migration);
//
//        tables = getTables();
//        assertTrue("must contain table 'schematest'", tables.contains("schematest"));
//    }

//    @Test
//    @JUnitTemporaryDatabase(createSchema=false)
//    public void testMultipleChangelogs() throws Exception {
//        assertFalse(changelogExists());
//
//        final Migration migration = new Migration();
//        migration.setAdminUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setAdminPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setDatabaseUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setDatabasePassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setChangeLog("changelog.xml");
//
//        final Migrator m = new Migrator();
//        m.setDataSource(m_dataSource);
//        m.setAdminDataSource(m_dataSource);
//        m.setValidateDatabaseVersion(false);
//        m.setCreateUser(false);
//        m.setCreateDatabase(false);
//
//        // Add a resource accessor to the migration so that it will load multiple changelog.xml files
//        // from the classpath
//        for (final Resource resource : getTestResources()) {
//            URI uri = resource.getURI();
//            if (uri.getScheme().equals("jar") && !uri.toString().contains("test-api.schema")) continue;
//            if (uri.getScheme().equals("file") && !uri.toString().contains("test-api/schema")) continue;
//            LOG.info("=== found resource: {} ===", resource);
//            migration.setAccessor(new ExistingResourceAccessor(resource));
//            m.migrate(migration);
//        }
//
//        final List<ChangelogEntry> ids = getChangelogEntries();
//
//        assertTrue(ids.size() > 0);
//        assertEquals("test-api.schema.a", ids.get(0).getId());
//        assertEquals("test-api.schema.b", ids.get(1).getId());
//    }

    @Test
    @JUnitTemporaryDatabase(createSchema=false)
    public void testRealChangelog() throws Exception {
        final Migration migration = new Migration();
        migration.setAdminUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
        migration.setAdminPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
        migration.setDatabaseUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
        migration.setDatabasePassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
        migration.setDatabaseName(TemporaryDatabasePostgreSQL.INTEGRATION_TEST_TEMPLATE_DB_NAME);
        migration.setChangeLog("changelog.xml");
        
        m_dataSource = new SimpleDataSource("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/" + TemporaryDatabasePostgreSQL.INTEGRATION_TEST_TEMPLATE_DB_NAME, migration.getDatabaseUser(), migration.getDatabasePassword());
        DataSource adminDataSource = new SimpleDataSource("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/template1", migration.getDatabaseUser(), migration.getDatabasePassword());

        final Migrator m = new Migrator();
        m.setDataSource(m_dataSource);
        m.setAdminDataSource(adminDataSource);
        m.setValidateDatabaseVersion(true);
        m.setCreateUser(false);
        m.setCreateDatabase(true);
        
        if (m.databaseExists(migration)) {
            m.databaseRemoveDB(migration);
        }
        
        System.out.println("blam");
        System.err.println("blarm!");

        m.prepareDatabase(migration);

        assertFalse(changelogExists());
        m.checkUnicode(migration);
        m.updateIplike();

        // Add a resource accessor to the migration so that it will load multiple changelog.xml files
        // from the classpath
        for (final Resource resource : getRealChangelog()) {
            LOG.info("=== found resource: {} ===", resource);
            migration.setAccessor(new ExistingResourceAccessor(resource));
            m.migrate(migration);
        }

        final List<ChangelogEntry> ids = getChangelogEntries();
        assertTrue(ids.size() > 0);
        // Check to make sure some of the changelogs ran
        assertTrue(ids.stream().anyMatch(id -> "17.0.0-remove-legacy-ipinterface-composite-key-fields".equals(id.getId())));
        assertTrue(ids.stream().anyMatch(id -> "17.0.0-remove-legacy-outages-composite-key-fields".equals(id.getId())));
    }

//    @Test
//    @JUnitTemporaryDatabase(createSchema=false)
//    public void testUpdateTwice() throws Exception {
//        assertFalse(changelogExists());
//
//        doMigration();
//        doMigration();
//    }

//    private void doMigration() throws MigrationException, IOException {
//        final Migration migration = new Migration();
//        migration.setAdminUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setAdminPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setDatabaseUser(System.getProperty(TemporaryDatabase.ADMIN_USER_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_USER));
//        migration.setDatabasePassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));
//        migration.setChangeLog("changelog.xml");
//
//        final Migrator m = new Migrator();
//        m.setDataSource(m_dataSource);
//
//        for (final Resource resource : getTestResources()) {
//            migration.setAccessor(new ExistingResourceAccessor(resource));
//            m.migrate(migration);
//        }
//    }

    protected boolean changelogExists() throws SQLException {
        final Connection connection = m_dataSource.getConnection();
        boolean exists = false;
        try {
            connection.prepareStatement("SELECT id FROM databasechangelog").execute();
            exists = true;
        } catch (SQLException e) {
        } finally {
            connection.close();
        }
        return exists;
    }

    protected Set<String> getStoredProcedures() throws SQLException {
        final Connection connection = m_dataSource.getConnection();
        final Set<String> procs = new HashSet<String>();
        try {
            final ResultSet rs = connection.getMetaData().getProcedures(null, null, "%");
            while (rs.next()) {
                procs.add(rs.getString("PROCEDURE_NAME").toLowerCase());
            }
        } finally {
            connection.close();
        }
        return procs;
    }

    protected Set<String> getTables() throws SQLException {
        final Connection connection = m_dataSource.getConnection();
        final Set<String> tables = new HashSet<String>();
        try {
            final ResultSet rs = connection.getMetaData().getTables(null, null, "%", null);
            while (rs.next()) {
                final String tableName = rs.getString("TABLE_NAME").toLowerCase();
                tables.add(tableName);
            }
        } finally {
            connection.close();
        }
        return tables;
    }

    private List<ChangelogEntry> getChangelogEntries() throws SQLException {
        final Connection connection = m_dataSource.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id, md5sum FROM databasechangelog order by id");
            assertTrue(statement.execute());
            ResultSet rs = statement.getResultSet();
            final List<ChangelogEntry> entries = new ArrayList<ChangelogEntry>();
            while (rs.next()) {
                entries.add(new ChangelogEntry(rs.getString(1), rs.getString(2)));
            }
            return entries;
        } finally {
            connection.close();
        }
    }

    private static class ChangelogEntry {
        private final String m_id;
        private final String m_md5sum;

        public ChangelogEntry(final String id, final String md5sum) {
            m_id = id;
            m_md5sum = md5sum;
        }

        public String getId() {
            return m_id;
        }

        @SuppressWarnings("unused")
        public String getMd5sum() {
            return m_md5sum;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
            result = prime * result + ((m_md5sum == null) ? 0 : m_md5sum.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final ChangelogEntry other = (ChangelogEntry) obj;
            if (m_id == null) {
                if (other.m_id != null) return false;
            } else if (!m_id.equals(other.m_id)) {
                return false;
            }
            if (m_md5sum == null) {
                if (other.m_md5sum != null) return false;
            } else if (!m_md5sum.equals(other.m_md5sum)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return m_id + "=" + m_md5sum;
        }
    }

//    private List<Resource> getTestResources() throws IOException {
//        final List<Resource> resources = new ArrayList<Resource>();
//        for (final Resource resource : m_context.getResources("classpath*:/changelog.xml")) {
//            URI uri = resource.getURI();
//            if (uri.getScheme().equals("file") && !uri.toString().contains("test-api/schema")) continue;
//            if (uri.getScheme().equals("jar") && !uri.toString().contains("test-api.schema")) continue;
//            resources.add(resource);
//        }
//        return resources;
//    }

    private List<Resource> getRealChangelog() throws IOException {
        final List<Resource> resources = new ArrayList<Resource>();
        for (final Resource resource : m_context.getResources("classpath*:/changelog.xml")) {
            URI uri = resource.getURI();
            System.err.println(uri.toString());
            if (uri.getScheme().equals("file") && !uri.toString().contains("opennms/core/schema")) continue;
            if (uri.getScheme().equals("jar") && !uri.toString().contains("opennms.core.schema")) continue;
            resources.add(resource);
        }
        return resources;
    }
}
