package com.compulynx.compas.models.extras;

public interface UserGroupRights {
  Long getRightId();
  String getRightName();
  boolean getAllowView();
  boolean getAllowAdd();
  boolean getAllowEdit();
  boolean getAllowDelete();
  String getPath();
}
