package com.example.application.tools;

import com.example.application.data.Role;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetToListConverter implements Converter<Set<Role>, List<Role>> {

    /**
     * Converts a {@link Set} of {@link Role} objects to a {@link List} of
     * {@link Role} objects.
     *
     * @param set the set of roles to convert
     * @param valueContext the value context provided by the Binder
     * @return a {@link Result} containing the list of roles. The result is
     *         always {@link Result isPresent() present}.
     */
    @Override
    public Result<List<Role>> convertToModel(Set<Role> set, ValueContext valueContext) {
        // Convert Set<Role> to List<Role>
        List<Role> list = new ArrayList<>(set);
        return Result.ok(list);
    }

    /**
     * {@link Role} objects.
     *
     * @param list the list of roles to convert
     * @param valueContext the value context provided by the Binder
     * @return a {@link Set} of roles. The returned set is always
     *         {@link Set#isEmpty() non-empty}.
     */

    @Override
    public Set<Role> convertToPresentation(List<Role> list, ValueContext valueContext) {
        // Convert List<Role> to Set<Role>
        Set<Role> set = new HashSet<>(list);
        return set;
    }

}