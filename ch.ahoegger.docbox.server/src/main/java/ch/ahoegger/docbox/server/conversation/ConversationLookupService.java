package ch.ahoegger.docbox.server.conversation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.conversation.IConversationLookupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link ConversationLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class ConversationLookupService extends AbstractDocboxLookupService<BigDecimal> implements IConversationLookupService {

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(Conversation.CONVERSATION.CONVERSATION_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    return getData(Conversation.CONVERSATION.NAME.likeIgnoreCase(call.getText()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByAllInternal(ILookupCall<BigDecimal> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByRecInternal(ILookupCall<BigDecimal> call) {
    return null;
  }

  protected List<? extends ILookupRow<BigDecimal>> getData(Condition conditions, ILookupCall<BigDecimal> call) {
    Conversation t = Conversation.CONVERSATION;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.CONVERSATION_NR, t.NAME, t.START_DATE, t.END_DATE)
        .from(t)
        .where(conditions)
        .fetch()
        .stream()
        .map(rec -> {
          LookupRow<BigDecimal> row = new LookupRow<BigDecimal>(rec.get(t.CONVERSATION_NR), rec.get(t.NAME));
          row.withActive(Optional.ofNullable(rec.get(t.END_DATE)).map(d -> LocalDateUtility.toLocalDate(d).plusDays(1)).orElse(LocalDate.now().plusDays(1)).isAfter(LocalDate.now()));

          return row;

        })
        .filter(r -> {
          if (call.getActive().isUndefined()) {
            return true;
          }
          else if (call.getActive().isTrue()) {
            return r.isActive();
          }
          else {
            return !r.isActive();
          }
        })
        .collect(Collectors.toList());
  }

}
