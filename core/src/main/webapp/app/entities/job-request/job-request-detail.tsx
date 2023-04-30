import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-request.reducer';

export const JobRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobRequestEntity = useAppSelector(state => state.core.jobRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobRequestDetailsHeading">
          <Translate contentKey="coreApp.jobRequest.detail.title">JobRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.id}</dd>
          <dt>
            <span id="consumer">
              <Translate contentKey="coreApp.jobRequest.consumer">Consumer</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.consumer}</dd>
          <dt>
            <span id="provider">
              <Translate contentKey="coreApp.jobRequest.provider">Provider</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.provider}</dd>
          <dt>
            <span id="serviceStatus">
              <Translate contentKey="coreApp.jobRequest.serviceStatus">Service Status</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.serviceStatus}</dd>
          <dt>
            <span id="roomId">
              <Translate contentKey="coreApp.jobRequest.roomId">Room Id</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.roomId}</dd>
        </dl>
        <Button tag={Link} to="/job-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-request/${jobRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobRequestDetail;
