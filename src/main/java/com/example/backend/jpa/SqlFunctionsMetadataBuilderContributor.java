package com.example.backend.jpa;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public class SqlFunctionsMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction("search_title",
                new SQLFunctionTemplate(BooleanType.INSTANCE,
                        "to_tsvector('russian', title) @@ plainto_tsquery('russian', ?1)"));
    }
}
