package com.psa.ml.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSpaceDetails {
	private String title;
	private int account_id;
	private boolean archived;
	private String description;
	private String due_date;
	private String effective_due_date;
	private String start_date;
	private boolean budgeted;
	private boolean change_orders_enabled;
	private String updated_at;
	private String created_at;
	private String consultant_role_name;
	private String client_role_name;
	private int percentage_complete;
	private String access_level;
	private boolean exclude_archived_stories_percent_complete;
	private boolean show_nonbillable_time_on_invoices;
	private boolean can_create_line_items;
	private String default_rate;
	private String currency;
	private String currency_symbol;
	private int currency_base_unit;
	private boolean can_invite;
	private boolean has_budget_access;
	private boolean tasks_default_non_billable;
	private int rate_card_id;
	private String workspace_invoice_preference_id;
	private boolean posts_require_privacy_decision;
	private boolean require_time_approvals;
	private boolean require_expense_approvals;
	private boolean has_active_timesheet_submissions;
	private boolean has_active_expense_report_submissions;
	//private Status status;
	private List<String> update_whitelist;
	//private AccountFeatures accountFeatures;
	//private Permissions permissions;
	private boolean over_budget;
	private boolean expenses_in_burn_rate;
	private int total_expenses_in_cents;
	private String price_in_cents;
	private String price;
	private int percent_of_budget_used;
	private String budget_used;
	private Integer budget_used_in_cents;
	private String budget_remaining;
	private Double target_margin;
	private boolean stories_are_fixed_fee_by_default;
	private String creator_id;
	private String primary_maven_id;
	private List<String> custom_field_value_ids;
	private String id;
	private String customFieldId;
}
