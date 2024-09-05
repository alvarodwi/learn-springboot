import * as React from 'react';
import GameApiClient from '../services/GameApiClient';
import ChallengesApiClient from '../services/ChallengeApiClient';

class LeaderBoardComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            leaderboard: [],
            serverError: false
        }
    }

    componentDidMount() {
        this.refreshLeaderBoard();
        // sets a timer to refresh the leaderboard every 5 seconds
        setInterval(this.refreshLeaderBoard.bind(this), 5000);
    }

    getLeaderBoardData(): Promise {
        return GameApiClient.leaderBoard().then(
            lbRes => {
                if (lbRes.ok) {
                    return lbRes.json();
                } else {
                    return Promise.reject("Gamification: error response");
                }
            }
        );
    }

    getAccountAliasData(accountIds: number[]): Promise {
        return ChallengesApiClient.getAccounts(accountIds).then(
            usRes => {
                if(usRes.ok) {
                    return usRes.json();
                } else {
                    return Promise.reject("Multiplication: error response");
                }
            }
        )
    }

    updateLeaderBoard(lb) {
        this.setState({
            leaderboard: lb,
            // reset the flag
            serverError: false
        });
    }

    refreshLeaderBoard() {
        this.getLeaderBoardData().then(
            lbData => {
                let accountIds = lbData.map(row => row.accountId);
                if(accountIds.length > 0) {
                    this.getAccountAliasData(accountIds).then(data => {
                        // build a map of id -> alias
                        let accountMap = new Map();
                        data.forEach(idAlias => {
                            accountMap.set(idAlias.id, idAlias.alias);
                        });
                        // add a property to existing lb data
                        lbData.forEach(row =>
                            row['alias'] = accountMap.get(row.accountId)
                        );
                        this.updateLeaderBoard(lbData);
                    }).catch(reason => {
                        console.log('Error mapping account ids', reason);
                        this.updateLeaderBoard(lbData);
                    });
                }
            }
        ).catch(reason => {
            this.setState({ serverError: true });
            console.log('Gamification server error', reason);
        });
    }

    render() {
        if (this.state.serverError) {
            return (
                <div>We're sorry, but we can't display game statistics at this
                    moment.</div>
            );
        }
        return (
            <div>
                <h3>Leaderboard</h3>
                <table>
                    <thead>
                    <tr>
                        <th>Account</th>
                        <th>Score</th>
                        <th>Badges</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.leaderboard.map(row => <tr key={row.accountId}>
                        <td>{row.alias ? row.alias : row.accountId}</td>
                        <td>{row.totalScore}</td>
                        <td>{row.badges.map(
                            b => <span className="badge" key={b}>{b}</span>)}
                        </td>
                    </tr>)}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default LeaderBoardComponent;