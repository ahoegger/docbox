package ch.ahoegger.docbox.server.hr.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupCodeType.UnbilledCode;
import ch.ahoegger.docbox.shared.hr.entity.EntityFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntitySearchFormData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData;
import ch.ahoegger.docbox.shared.hr.entity.EntityTablePageData.EntityTableRowData;
import ch.ahoegger.docbox.shared.hr.entity.IEntityService;
import ch.ahoegger.docbox.shared.hr.entity.IEntityTable;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

public class EntityService implements IEntityService, IEntityTable {

  @Override
  public EntityTablePageData getEntityTableData(EntitySearchFormData formData) {
    List<Object> binds = new ArrayList<>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, ENTITY_NR, PARTNER_NR, POSTING_GROUP_NR, ENTITY_TYPE, ENTITY_DATE, HOURS, AMOUNT, DESCRIPTION))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);

    // search partnerId
    if (formData.getPartnerId().getValue() != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, PARTNER_NR)).append(" = :partnerId");
      binds.add(new NVPair("partnerId", formData.getPartnerId().getValue()));
    }

    // search postingGroupId
    if (formData.getPostingGroupId() == null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, POSTING_GROUP_NR)).append(" IS NULL");
    }
    else {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, POSTING_GROUP_NR)).append(" = :postingGroupId");
      binds.add(new NVPair("postingGroupId", formData.getPostingGroupId()));
    }

    // entity date
    // document date from
    if (formData.getEntityDateFrom().getValue() != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, ENTITY_DATE)).append(" >= ").append(":entityDateFrom");
    }
    // document date to
    if (formData.getEntityDateTo().getValue() != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, ENTITY_DATE)).append(" <= ").append(":entityDateTo");
    }

    if (CollectionUtility.hasElements(formData.getEntityIds())) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, ENTITY_NR)).append(" IN (")
          .append(
              formData.getEntityIds()
                  .stream()
                  .filter(key -> key != null)
                  .map(key -> key.toString())
                  .collect(Collectors.joining(", ")))
          .append(")");
    }

    statementBuilder.append(" INTO ")
        .append(":{td.").append(EntityTableRowData.enityId).append("}, ")
        .append(":{td.").append(EntityTableRowData.partnerId).append("}, ")
        .append(":{td.").append(EntityTableRowData.postingGroupId).append("}, ")
        .append(":{td.").append(EntityTableRowData.entityType).append("}, ")
        .append(":{td.").append(EntityTableRowData.date).append("}, ")
        .append(":{td.").append(EntityTableRowData.hours).append("}, ")
        .append(":{td.").append(EntityTableRowData.amount).append("}, ")
        .append(":{td.").append(EntityTableRowData.text).append("} ");

    EntityTablePageData tableData = new EntityTablePageData();
    binds.add(new NVPair("td", tableData));
    binds.add(formData);
    SQL.selectInto(statementBuilder.toString(),
        binds.toArray());
    return tableData;
  }

  @Override
  public EntityFormData prepareCreate(EntityFormData formData) {
    LocalDate today = LocalDate.now();
    formData.getEntityDate().setValue(LocalDateUtility.toDate(today));
    formData.setPostingGroupId(UnbilledCode.ID);
    return formData;
  }

  @Override
  public EntityFormData create(EntityFormData formData) {
    formData.setEntityId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(ENTITY_NR, PARTNER_NR, POSTING_GROUP_NR, ENTITY_TYPE, HOURS, AMOUNT, ENTITY_DATE, DESCRIPTION)).append(")");
    statementBuilder.append(" VALUES (:entityId, :partnerId,:postingGroupId, :entityType, :workHours, :expenseAmount, :entityDate, :text)");
    SQL.insert(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public EntityFormData load(EntityFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columns(PARTNER_NR, POSTING_GROUP_NR, ENTITY_TYPE, HOURS, AMOUNT, ENTITY_DATE, DESCRIPTION)));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(ENTITY_NR).append(" = :entityId");
    statementBuilder.append(" INTO :partnerId, :postingGroupId, :entityType, :workHours, :expenseAmount, :entityDate, :text");
    SQL.selectInto(statementBuilder.toString(), formData);
    return formData;
  }

  @Override
  public EntityFormData store(EntityFormData formData) {
    if (ObjectUtility.notEquals(UnbilledCode.ID, formData.getPostingGroupId())) {
      throw new VetoException("Access denied.");
    }
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
    statementBuilder.append(PARTNER_NR).append("= :partnerId, ");
    statementBuilder.append(POSTING_GROUP_NR).append("= :postingGroupId, ");
    statementBuilder.append(ENTITY_TYPE).append("= :entityType, ");
    statementBuilder.append(HOURS).append("= :workHours, ");
    statementBuilder.append(AMOUNT).append("= :expenseAmount, ");
    statementBuilder.append(ENTITY_DATE).append("= :entityDate, ");
    statementBuilder.append(DESCRIPTION).append("= :text ");
    statementBuilder.append(" WHERE ").append(ENTITY_NR).append(" = :entityId");
    SQL.update(statementBuilder.toString(), formData);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  public void updateGroupId(Set<BigDecimal> entityIds, BigDecimal groupId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ")
        .append(POSTING_GROUP_NR).append("= :groupId ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);
    if (CollectionUtility.hasElements(entityIds)) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.whereIn(ENTITY_NR, entityIds));
    }
    SQL.update(statementBuilder.toString(), new NVPair("groupId", groupId));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
  }
}
