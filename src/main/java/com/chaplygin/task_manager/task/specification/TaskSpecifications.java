package com.chaplygin.task_manager.task.specification;

import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasTitle(String title) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Task> hasDescription(String description) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Task> hasStatus(Status status) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                priority == null ? null : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasOwner(Long ownerId) {
        return (root, query, criteriaBuilder) ->
                ownerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, criteriaBuilder) ->
                assigneeId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> hasOwnerOrAssignee(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Predicate ownerPredicate = criteriaBuilder.equal(root.get("owner").get("id"), userId);
            Predicate assigneePredicate = criteriaBuilder.equal(root.get("assignee").get("id"), userId);
            return criteriaBuilder.or(ownerPredicate, assigneePredicate);
        };
    }
}
