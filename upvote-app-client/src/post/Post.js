import React, { Component } from 'react';
import './Post.css';
import { Avatar, Icon } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

import { Radio, Button } from 'antd';
const RadioGroup = Radio.Group;

class Post extends Component {
    calculatePercentage = (choice) => {
        if(this.props.post.totalVotes === 0) {
            return 0;
        }
        return (choice.voteCount*100)/(this.props.post.totalVotes);
    };

    isSelected = (choice) => {
        return this.props.post.selectedChoice === choice.id;
    }

    getWinningChoice = () => {
        return this.props.post.choices.reduce((prevChoice, currentChoice) => 
            currentChoice.voteCount > prevChoice.voteCount ? currentChoice : prevChoice, 
            {voteCount: -Infinity}
        );
    }

    getTimeRemaining = (post) => {
        const expirationTime = new Date(post.expirationDateTime).getTime();
        const currentTime = new Date().getTime();
    
        var difference_ms = expirationTime - currentTime;
        var seconds = Math.floor( (difference_ms/1000) % 60 );
        var minutes = Math.floor( (difference_ms/1000/60) % 60 );
        var hours = Math.floor( (difference_ms/(1000*60*60)) % 24 );
        var days = Math.floor( difference_ms/(1000*60*60*24) );
    
        let timeRemaining;
    
        if(days > 0) {
            timeRemaining = days + " days left";
        } else if (hours > 0) {
            timeRemaining = hours + " hours left";
        } else if (minutes > 0) {
            timeRemaining = minutes + " minutes left";
        } else if(seconds > 0) {
            timeRemaining = seconds + " seconds left";
        } else {
            timeRemaining = "less than a second left";
        }
        
        return timeRemaining;
    }

    render() {
        const postChoices = [];
        if(this.props.post.selectedChoice || this.props.post.expired) {
            const winningChoice = this.props.post.expired ? this.getWinningChoice() : null;

            this.props.post.choices.forEach(choice => {
                postChoices.push(<CompletedOrVotedPostChoice 
                    key={choice.id} 
                    choice={choice}
                    isWinner={winningChoice && choice.id === winningChoice.id}
                    isSelected={this.isSelected(choice)}
                    percentVote={this.calculatePercentage(choice)} 
                />);
            });                
        } else {
            this.props.post.choices.forEach(choice => {
                postChoices.push(<Radio className="post-choice-radio" key={choice.id} value={choice.id}>{choice.text}</Radio>)
            })    
        }        
        return (
            <div className="post-content">
                <div className="post-header">
                    <div className="post-creator-info">
                        <Link className="creator-link" to={`/users/${this.props.post.createdBy.username}`}>
                            <Avatar className="post-creator-avatar" 
                                style={{ backgroundColor: getAvatarColor(this.props.post.createdBy.name)}} >
                                {this.props.post.createdBy.name[0].toUpperCase()}
                            </Avatar>
                            <span className="post-creator-name">
                                {this.props.post.createdBy.name}
                            </span>
                            <span className="post-creator-username">
                                @{this.props.post.createdBy.username}
                            </span>
                            <span className="post-creation-date">
                                {formatDateTime(this.props.post.creationDateTime)}
                            </span>
                        </Link>
                    </div>
                    <div className="post-question">
                        {this.props.post.question}
                    </div>
                </div>
                <div className="post-choices">
                    <RadioGroup 
                        className="post-choice-radio-group" 
                        onChange={this.props.handleVoteChange} 
                        value={this.props.currentVote}>
                        { postChoices }
                    </RadioGroup>
                </div>
                <div className="post-footer">
                    { 
                        !(this.props.post.selectedChoice || this.props.post.expired) ?
                        (<Button className="vote-button" disabled={!this.props.currentVote} onClick={this.props.handleVoteSubmit}>Vote</Button>) : null 
                    }
                    <span className="total-votes">{this.props.post.totalVotes} votes</span>
                    <span className="separator">•</span>
                    <span className="time-left">
                        {
                            this.props.post.expired ? "Final results" :
                            this.getTimeRemaining(this.props.post)
                        }
                    </span>
                </div>
            </div>
        );
    }
}

function CompletedOrVotedPostChoice(props) {
    return (
        <div className="cv-post-choice">
            <span className="cv-post-choice-details">
                <span className="cv-choice-percentage">
                    {Math.round(props.percentVote * 100) / 100}%
                </span>            
                <span className="cv-choice-text">
                    {props.choice.text}
                </span>
                {
                    props.isSelected ? (
                    <Icon
                        className="selected-choice-icon"
                        type="check-circle-o"
                    /> ): null
                }    
            </span>
            <span className={props.isWinner ? 'cv-choice-percent-chart winner': 'cv-choice-percent-chart'} 
                style={{width: props.percentVote + '%' }}>
            </span>
        </div>
    );
}


export default Post;