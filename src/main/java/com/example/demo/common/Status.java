package com.example.demo.common;

/**
 * Shared status for entities (e.g. Address, or any other table).
 * Use ARCHIVE for soft-deleted / hidden, PUBLISHED for active.
 */
public enum Status {
    ARCHIVE,
    PUBLISHED
}
