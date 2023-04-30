import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChaleurs } from 'app/shared/model/sensoring/chaleurs.model';
import { getEntities } from './chaleurs.reducer';

export const Chaleurs = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const chaleursList = useAppSelector(state => state.core.chaleurs.entities);
  const loading = useAppSelector(state => state.core.chaleurs.loading);
  const totalItems = useAppSelector(state => state.core.chaleurs.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="chaleurs-heading" data-cy="ChaleursHeading">
        <Translate contentKey="coreApp.sensoringChaleurs.home.title">Chaleurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coreApp.sensoringChaleurs.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/chaleurs/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coreApp.sensoringChaleurs.home.createLabel">Create new Chaleurs</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {chaleursList && chaleursList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.date">Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('jrsLact')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.jrsLact">Jrs Lact</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('temps')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.temps">Temps</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('groupeid')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.groupeid">Groupeid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('enclosid')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.enclosid">Enclosid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('activite')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.activite">Activite</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('facteurEleve')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.facteurEleve">Facteur Eleve</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('suspect')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.suspect">Suspect</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actAugmentee')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.actAugmentee">Act Augmentee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('alarmeChaleur')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.alarmeChaleur">Alarme Chaleur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pasDeChaleur')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.pasDeChaleur">Pas De Chaleur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('cowId')}>
                  <Translate contentKey="coreApp.sensoringChaleurs.cowId">Cow Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {chaleursList.map((chaleurs, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/chaleurs/${chaleurs.id}`} color="link" size="sm">
                      {chaleurs.id}
                    </Button>
                  </td>
                  <td>{chaleurs.date ? <TextFormat type="date" value={chaleurs.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{chaleurs.jrsLact}</td>
                  <td>{chaleurs.temps}</td>
                  <td>{chaleurs.groupeid}</td>
                  <td>{chaleurs.enclosid}</td>
                  <td>{chaleurs.activite}</td>
                  <td>{chaleurs.facteurEleve}</td>
                  <td>{chaleurs.suspect}</td>
                  <td>{chaleurs.actAugmentee}</td>
                  <td>{chaleurs.alarmeChaleur}</td>
                  <td>{chaleurs.pasDeChaleur}</td>
                  <td>{chaleurs.cowId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/chaleurs/${chaleurs.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/chaleurs/${chaleurs.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/chaleurs/${chaleurs.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coreApp.sensoringChaleurs.home.notFound">No Chaleurs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={chaleursList && chaleursList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Chaleurs;
