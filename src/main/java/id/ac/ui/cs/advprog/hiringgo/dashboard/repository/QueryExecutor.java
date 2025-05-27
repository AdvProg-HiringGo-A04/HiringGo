package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;

public class QueryExecutor {

    public long executeCountQuery(EntityManager entityManager, String queryString,
                                  String paramName, Object paramValue, Logger log) {
        try {
            Object result = entityManager.createQuery(queryString)
                    .setParameter(paramName, paramValue)
                    .getSingleResult();
            return result != null ? (Long) result : 0L;
        } catch (Exception e) {
            log.error("Error executing count query: {}", e.getMessage());
            return 0L;
        }
    }

    public long executeSimpleCountQuery(EntityManager entityManager, String queryString, Logger log) {
        try {
            Object result = entityManager.createQuery(queryString).getSingleResult();
            return result != null ? (Long) result : 0L;
        } catch (Exception e) {
            log.error("Error executing simple count query: {}", e.getMessage());
            return 0L;
        }
    }
}