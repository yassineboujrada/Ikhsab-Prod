import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMessage } from 'app/shared/model/notification/message.model';
import { MessageType } from 'app/shared/model/enumerations/message-type.model';
import { getEntity, updateEntity, createEntity, reset } from './message.reducer';

export const MessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const messageEntity = useAppSelector(state => state.core.message.entity);
  const loading = useAppSelector(state => state.core.message.loading);
  const updating = useAppSelector(state => state.core.message.updating);
  const updateSuccess = useAppSelector(state => state.core.message.updateSuccess);
  const messageTypeValues = Object.keys(MessageType);

  const handleClose = () => {
    navigate('/message' + location.search);
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
    values.createdDateTime = convertDateTimeToServer(values.createdDateTime);

    const entity = {
      ...messageEntity,
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
      ? {
          createdDateTime: displayDefaultDateTime(),
        }
      : {
          messageType: 'CLIENT',
          ...messageEntity,
          createdDateTime: convertDateTimeFromServer(messageEntity.createdDateTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.notificationMessage.home.createOrEditLabel" data-cy="MessageCreateUpdateHeading">
            <Translate contentKey="coreApp.notificationMessage.home.createOrEditLabel">Create or edit a Message</Translate>
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
                  id="message-id"
                  label={translate('coreApp.notificationMessage.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.notificationMessage.createdDateTime')}
                id="message-createdDateTime"
                name="createdDateTime"
                data-cy="createdDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.notificationMessage.messageType')}
                id="message-messageType"
                name="messageType"
                data-cy="messageType"
                type="select"
              >
                {messageTypeValues.map(messageType => (
                  <option value={messageType} key={messageType}>
                    {translate('coreApp.MessageType.' + messageType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coreApp.notificationMessage.content')}
                id="message-content"
                name="content"
                data-cy="content"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.notificationMessage.room')}
                id="message-room"
                name="room"
                data-cy="room"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.notificationMessage.username')}
                id="message-username"
                name="username"
                data-cy="username"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message" replace color="info">
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

export default MessageUpdate;
