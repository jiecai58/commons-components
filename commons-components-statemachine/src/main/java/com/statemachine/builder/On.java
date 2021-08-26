package com.statemachine.builder;

import com.statemachine.Condition;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S, E, C> extends com.statemachine.builder.When<S, E, C> {
    /**
     * Add condition for the transition
     * @param condition transition condition
     * @return When clause builder
     */
    com.statemachine.builder.When<S, E, C> when(Condition<C> condition);
}
