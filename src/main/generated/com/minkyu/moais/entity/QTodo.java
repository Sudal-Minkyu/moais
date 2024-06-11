package com.minkyu.moais.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTodo is a Querydsl query type for Todo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTodo extends EntityPathBase<Todo> {

    private static final long serialVersionUID = -985272096L;

    public static final QTodo todo = new QTodo("todo");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath tdComment = createString("tdComment");

    public final NumberPath<Integer> tdState = createNumber("tdState", Integer.class);

    public final StringPath tdYyyymmdd = createString("tdYyyymmdd");

    public final NumberPath<Long> todoId = createNumber("todoId", Long.class);

    public QTodo(String variable) {
        super(Todo.class, forVariable(variable));
    }

    public QTodo(Path<? extends Todo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTodo(PathMetadata metadata) {
        super(Todo.class, metadata);
    }

}

