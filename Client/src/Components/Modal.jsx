import React from 'react'

export default function Modal(props) {
    return (
        <div class="modal fade" id="staticModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="staticBackdropLabel">Modal title</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      {props.statement}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onClick={()=>props.onCancelHandler}>Cancel</button>
                        <button type="button" data-bs-dismiss="modal" class="btn btn-success" onClick={()=>props.confirmHandler(props.selected)}>Confirm</button>
                    </div>
                </div>
            </div>
        </div>
  )
}
