import React, { useState } from 'react';
import PropTypes from 'prop-types';
import '../styles/editabletextarea.css';

const EditableTextArea = ({ initialText = '', onSave, isToggleable = false, charLimit = -1 }) => {
  const [bio, setBio] = useState(initialText);
  const [unsavedBio, setUnsavedBio] = useState(bio);
  const [editable, setEditable] = useState(false);

  const handleSave = () => {
    const shouldSave = editable;
    setEditable(!editable);
    setBio(unsavedBio);
    if (shouldSave) {
      onSave(unsavedBio);
    }
  };

  const handleChange = (text) => {
    let newText = text;
    if (charLimit > 0) {
      newText = newText.substring(0, Math.min(text.length, charLimit));
    }
    setUnsavedBio(newText);
  };

  const handleCancel = () => {
    setEditable(!editable);
    setUnsavedBio(bio);
  };

  return (
    <div className="edit-display-wrapper">
      {editable ? (
        <textarea
          data-testid="display-textarea"
          className="display-textarea-dark edit-text"
          onChange={(e) => handleChange(e.target.value)}
          value={unsavedBio}
        />
      ) : (
        <div data-testid="display-text" className="display-text-light display-text">
          {bio}
        </div>
      )}
      {isToggleable && (
        <div className="button-container" data-testid="button-container">
          {editable && (
            <button className="input-button bgh-red" onClick={handleCancel}>
              Cancel
            </button>
          )}
          <button className="input-button bgh-green" onClick={handleSave}>
            {editable ? 'Save' : 'Edit'}
          </button>
        </div>
      )}
    </div>
  );
};

EditableTextArea.propTypes = {
  initialText: PropTypes.string,
  onSave: PropTypes.func.isRequired,
  isToggleable: PropTypes.bool,
  charLimit: PropTypes.number,
};

export default EditableTextArea;
