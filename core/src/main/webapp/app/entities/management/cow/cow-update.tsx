import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICow } from 'app/shared/model/management/cow.model';
import { getEntity, updateEntity, createEntity, reset } from './cow.reducer';

export const CowUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cowEntity = useAppSelector(state => state.core.cow.entity);
  const loading = useAppSelector(state => state.core.cow.loading);
  const updating = useAppSelector(state => state.core.cow.updating);
  const updateSuccess = useAppSelector(state => state.core.cow.updateSuccess);

  const handleClose = () => {
    navigate('/cow' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cowEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...cowEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.managementCow.home.createOrEditLabel" data-cy="CowCreateUpdateHeading">
            <Translate contentKey="coreApp.managementCow.home.createOrEditLabel">Create or edit a Cow</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="cow-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.managementCow.numero')}
                id="cow-numero"
                name="numero"
                data-cy="numero"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCow.groupeId')}
                id="cow-groupeId"
                name="groupeId"
                data-cy="groupeId"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCow.enclosId')}
                id="cow-enclosId"
                name="enclosId"
                data-cy="enclosId"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCow.repondeur')}
                id="cow-repondeur"
                name="repondeur"
                data-cy="repondeur"
                type="text"
              />
              <ValidatedField label={translate('coreApp.managementCow.nom')} id="cow-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
                label={translate('coreApp.managementCow.deviceId')}
                id="cow-deviceId"
                name="deviceId"
                data-cy="deviceId"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCow.userId')}
                id="cow-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cow" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CowUpdate;
