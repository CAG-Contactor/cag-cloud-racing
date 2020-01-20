import * as React from 'react';
import './nav-bar.css';

export type NavBarSelections = 'Leaderboard' | 'CurrentRace';

export interface NavBarProps {
  readonly currentSelection: NavBarSelections
  readonly onChangedSelection: (viewSelection: NavBarSelections) => void
}

export const NavBar = (props: NavBarProps) => {
  const {currentSelection, onChangedSelection} = props;
  return (
    <nav className="navbar navbar-expand-md navbar-dark mb-3">

      <button style={{marginBottom: 10}} className="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span className="navbar-toggler-icon"/>
      </button>

      <div style={{textAlign:"left"}} className="collapse navbar-collapse justify-content-center" id="collapsibleNavbar">
        <ul className="navbar-nav">
          <li className="nav-item">
            <div className={`link ${navAnchorClass(currentSelection === 'Leaderboard')}`}
               onClick={changeSelection('Leaderboard')}>Leaderboard</div>
          </li>
          <li>
            <div className={`link ${navAnchorClass(currentSelection === 'CurrentRace')}`}
               onClick={changeSelection('CurrentRace')}>Current Race</div>
          </li>
        </ul>
      </div>
    </nav>
  );

  function changeSelection(newSelection: NavBarSelections) {
    return () => onChangedSelection(newSelection)
  }
};

function navAnchorClass(active: boolean) {
  return `nav-link ${active ? 'active' : ''}`;
}

