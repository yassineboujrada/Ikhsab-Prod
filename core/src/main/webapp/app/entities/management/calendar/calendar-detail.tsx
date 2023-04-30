import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './calendar.reducer';

export const CalendarDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const calendarEntity = useAppSelector(state => state.core.calendar.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="calendarDetailsHeading">
          <Translate contentKey="coreApp.managementCalendar.detail.title">Calendar</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.id}</dd>
          <dt>
            <span id="lactation">
              <Translate contentKey="coreApp.managementCalendar.lactation">Lactation</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.lactation}</dd>
          <dt>
            <span id="jrsLact">
              <Translate contentKey="coreApp.managementCalendar.jrsLact">Jrs Lact</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.jrsLact}</dd>
          <dt>
            <span id="statutReproduction">
              <Translate contentKey="coreApp.managementCalendar.statutReproduction">Statut Reproduction</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.statutReproduction}</dd>
          <dt>
            <span id="etatProd">
              <Translate contentKey="coreApp.managementCalendar.etatProd">Etat Prod</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.etatProd}</dd>
          <dt>
            <span id="dateNaissance">
              <Translate contentKey="coreApp.managementCalendar.dateNaissance">Date Naissance</Translate>
            </span>
          </dt>
          <dd>
            {calendarEntity.dateNaissance ? <TextFormat value={calendarEntity.dateNaissance} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="velage">
              <Translate contentKey="coreApp.managementCalendar.velage">Velage</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.velage ? <TextFormat value={calendarEntity.velage} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="chaleur">
              <Translate contentKey="coreApp.managementCalendar.chaleur">Chaleur</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.chaleur ? <TextFormat value={calendarEntity.chaleur} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="insemination">
              <Translate contentKey="coreApp.managementCalendar.insemination">Insemination</Translate>
            </span>
          </dt>
          <dd>
            {calendarEntity.insemination ? <TextFormat value={calendarEntity.insemination} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="cowId">
              <Translate contentKey="coreApp.managementCalendar.cowId">Cow Id</Translate>
            </span>
          </dt>
          <dd>{calendarEntity.cowId}</dd>
        </dl>
        <Button tag={Link} to="/calendar" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/calendar/${calendarEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CalendarDetail;
