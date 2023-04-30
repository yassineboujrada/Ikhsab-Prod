import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message.reducer';

export const MessageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageEntity = useAppSelector(state => state.core.message.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageDetailsHeading">
          <Translate contentKey="coreApp.notificationMessage.detail.title">Message</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="coreApp.notificationMessage.id">Id</Translate>
            </span>
          </dt>
          <dd>{messageEntity.id}</dd>
          <dt>
            <span id="createdDateTime">
              <Translate contentKey="coreApp.notificationMessage.createdDateTime">Created Date Time</Translate>
            </span>
          </dt>
          <dd>
            {messageEntity.createdDateTime ? (
              <TextFormat value={messageEntity.createdDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="messageType">
              <Translate contentKey="coreApp.notificationMessage.messageType">Message Type</Translate>
            </span>
          </dt>
          <dd>{messageEntity.messageType}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="coreApp.notificationMessage.content">Content</Translate>
            </span>
          </dt>
          <dd>{messageEntity.content}</dd>
          <dt>
            <span id="room">
              <Translate contentKey="coreApp.notificationMessage.room">Room</Translate>
            </span>
          </dt>
          <dd>{messageEntity.room}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="coreApp.notificationMessage.username">Username</Translate>
            </span>
          </dt>
          <dd>{messageEntity.username}</dd>
        </dl>
        <Button tag={Link} to="/message" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message/${messageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessageDetail;
