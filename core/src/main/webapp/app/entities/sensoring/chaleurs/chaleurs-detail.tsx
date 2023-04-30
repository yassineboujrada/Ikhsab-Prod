import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chaleurs.reducer';

export const ChaleursDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chaleursEntity = useAppSelector(state => state.core.chaleurs.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chaleursDetailsHeading">
          <Translate contentKey="coreApp.sensoringChaleurs.detail.title">Chaleurs</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="coreApp.sensoringChaleurs.date">Date</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.date ? <TextFormat value={chaleursEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="jrsLact">
              <Translate contentKey="coreApp.sensoringChaleurs.jrsLact">Jrs Lact</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.jrsLact}</dd>
          <dt>
            <span id="temps">
              <Translate contentKey="coreApp.sensoringChaleurs.temps">Temps</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.temps}</dd>
          <dt>
            <span id="groupeid">
              <Translate contentKey="coreApp.sensoringChaleurs.groupeid">Groupeid</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.groupeid}</dd>
          <dt>
            <span id="enclosid">
              <Translate contentKey="coreApp.sensoringChaleurs.enclosid">Enclosid</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.enclosid}</dd>
          <dt>
            <span id="activite">
              <Translate contentKey="coreApp.sensoringChaleurs.activite">Activite</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.activite}</dd>
          <dt>
            <span id="facteurEleve">
              <Translate contentKey="coreApp.sensoringChaleurs.facteurEleve">Facteur Eleve</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.facteurEleve}</dd>
          <dt>
            <span id="suspect">
              <Translate contentKey="coreApp.sensoringChaleurs.suspect">Suspect</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.suspect}</dd>
          <dt>
            <span id="actAugmentee">
              <Translate contentKey="coreApp.sensoringChaleurs.actAugmentee">Act Augmentee</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.actAugmentee}</dd>
          <dt>
            <span id="alarmeChaleur">
              <Translate contentKey="coreApp.sensoringChaleurs.alarmeChaleur">Alarme Chaleur</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.alarmeChaleur}</dd>
          <dt>
            <span id="pasDeChaleur">
              <Translate contentKey="coreApp.sensoringChaleurs.pasDeChaleur">Pas De Chaleur</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.pasDeChaleur}</dd>
          <dt>
            <span id="cowId">
              <Translate contentKey="coreApp.sensoringChaleurs.cowId">Cow Id</Translate>
            </span>
          </dt>
          <dd>{chaleursEntity.cowId}</dd>
        </dl>
        <Button tag={Link} to="/chaleurs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chaleurs/${chaleursEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChaleursDetail;
