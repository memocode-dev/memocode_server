package dev.memocode.domain.question;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.tag.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "question_tag")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class QuestionTag extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @EqualsAndHashCode.Include
    private Tag tag;

    @ManyToOne(fetch = LAZY)
    @EqualsAndHashCode.Include
    private Question question;

    protected void softDelete() {
        super.delete();
    }

    protected void recover() {
        super.recover();
    }
}
