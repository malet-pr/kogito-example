package sampleCode.testEntity;

import sampleCode.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="test_entity")
public class TestEntity extends BaseEntity {
    public String name;
    public String description;
}
