/*package com.jm.online_store.controller.rest;

public class TestDelete {
    @Override
    public List<EmployeeEvent> findByCriteria(EmployeeEventCriteria criteria, int take, int skip) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeEvent> criteriaQuery = criteriaBuilder.createQuery(EmployeeEvent.class);
        Root<EmployeeEvent> from = criteriaQuery.from(EmployeeEvent.class);
        criteriaQuery.where(buildPredicate(criteriaBuilder, criteria, from));
        Join employeeJoin = (Join) from.fetch("employee", JoinType.LEFT);
        from.fetch("gosService", JoinType.LEFT);
        Expression<String> pathOrder;
        switch (criteria.getOrderBy()){
            case "EMPLOYEE_ID": pathOrder = employeeJoin.get("personNumber"); break;
            case "INCIDENT_ID": pathOrder = from.get("incidentId"); break;
            case "EMPLOYEE_FIO": pathOrder = getConcatEmployeeFio(criteriaBuilder, employeeJoin); break;
            case "EVENT": pathOrder = from.get("eventType"); break;
            default: pathOrder = from.get("date"); break;
        }
        if(criteria.getDirection().equals(OrderBy.Direction.asc))
            criteriaQuery.orderBy(criteriaBuilder.asc(pathOrder));
        else
            criteriaQuery.orderBy(criteriaBuilder.desc(pathOrder));
        TypedQuery<EmployeeEvent> query = entityManager.createQuery(criteriaQuery);
        return query.setFirstResult(skip).setMaxResults(take).getResultList();
    }
}*/
