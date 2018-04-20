package com.ticketing.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * Immutable implementation of {@link User}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableUser.builder()}.
 */
@SuppressWarnings("all")
@Generated({"Immutables.generator", "User"})
public final class ImmutableUser implements User {
  private final String name;
  private final int age;

  private ImmutableUser(String name, int age) {
    this.name = name;
    this.age = age;
  }

  /**
   * @return The value of the {@code name} attribute
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * @return The value of the {@code age} attribute
   */
  @Override
  public int getAge() {
    return age;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getName() name} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param name A new value for name
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withName(String name) {
    if (this.name.equals(name)) return this;
    String newValue = Objects.requireNonNull(name, "name");
    return new ImmutableUser(newValue, this.age);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getAge() age} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param age A new value for age
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withAge(int age) {
    if (this.age == age) return this;
    return new ImmutableUser(this.name, age);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableUser} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableUser
        && equalTo((ImmutableUser) another);
  }

  private boolean equalTo(ImmutableUser another) {
    return name.equals(another.name)
        && age == another.age;
  }

  /**
   * Computes a hash code from attributes: {@code name}, {@code age}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + name.hashCode();
    h = h * 17 + age;
    return h;
  }

  /**
   * Prints the immutable value {@code User} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "User{"
        + "name=" + name
        + ", age=" + age
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link User} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable User instance
   */
  public static ImmutableUser copyOf(User instance) {
    if (instance instanceof ImmutableUser) {
      return (ImmutableUser) instance;
    }
    return ImmutableUser.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableUser ImmutableUser}.
   * @return A new ImmutableUser builder
   */
  public static ImmutableUser.Builder builder() {
    return new ImmutableUser.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableUser ImmutableUser}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  public static final class Builder {
    private static final long INIT_BIT_NAME = 0x1L;
    private static final long INIT_BIT_AGE = 0x2L;
    private long initBits = 0x3L;

    private String name;
    private int age;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code User} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(User instance) {
      Objects.requireNonNull(instance, "instance");
      name(instance.getName());
      age(instance.getAge());
      return this;
    }

    /**
     * Initializes the value for the {@link User#getName() name} attribute.
     * @param name The value for name 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder name(String name) {
      this.name = Objects.requireNonNull(name, "name");
      initBits &= ~INIT_BIT_NAME;
      return this;
    }

    /**
     * Initializes the value for the {@link User#getAge() age} attribute.
     * @param age The value for age 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder age(int age) {
      this.age = age;
      initBits &= ~INIT_BIT_AGE;
      return this;
    }

    /**
     * Builds a new {@link ImmutableUser ImmutableUser}.
     * @return An immutable instance of User
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableUser build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableUser(name, age);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<String>();
      if ((initBits & INIT_BIT_NAME) != 0) attributes.add("name");
      if ((initBits & INIT_BIT_AGE) != 0) attributes.add("age");
      return "Cannot build User, some of required attributes are not set " + attributes;
    }
  }
}
