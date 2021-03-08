/* Onload */
$(function() {
	$('input[data-mask]').each(function (i, e) {
		$(e).mask($(e).data('mask'));
	});
	$('.currency').maskMoney({ prefix: "R$ ", decimal: ",", thousands: "." });
	$('.datepicker').datepicker({
		language: 'pt-BR'
	});
	$('.sticky').floatThead();
});

/* Spinner functions */
function showModal(idModal, callback) {
	var id = idModal;
	$(id).on('shown.bs.modal', callback);
	$(id).modal('show');
}

function showSpinner(callback) {
	$('#spinner').fadeIn('fast', callback);
}

function hideSpinner(callback) {
	$('#spinner').fadeOut('fast', callback);
}

/* Toast functions */
function toast(message, type) {
	$.toaster(message, '', type);
}

/* Functions Planning */
function newPlanning() {
	$('#modal_planning_title').text('Novo planejamento');
	$('#planning_action').val('insert');
	$('#planning_id').val(0);
	$('#planning_title').val('');
	showModal('#modal_planning', function(e) {
		$('#planning_title').focus();
	});
}

function editPlanning(id) {
	var l = $(id);
	var title = l.find('td:nth-child(1)').text();
	$('#modal_planning_title').text(title);
	$('#planning_action').val('edit');
	$('#planning_id').val(l.data('id'));
	$('#planning_title').val(title);
	showModal('#modal_planning', function(e) {
		$('#planning_title').focus();
	});
}

/* Functions Process */
function newProcess(uid) {
	$('#modal_process_title').text('Novo processo');
	$('#modal_process_action').val('insert');
	$('#modal_process_id').val(0);
	$('#modal_process_unity').selectpicker('val', $('#modal_process_unity option:first').val());
	$('#modal_process_description').val('');
	$('#modal_process_nup').val('');
	$('#modal_process_pam').val('');
	$('#modal_process_tr').val('');
	$('#modal_process_date').val('');
	$('#modal_process_value').val('');
	$('#modal_process_type').selectpicker('val', $('#modal_process_type option:first').val());
	$('#modal_process_modality').selectpicker('val', $('#modal_process_modality option:first').val());
	$('#modal_process_info').val('');
	$('#modal_process_responsible').selectpicker('val', uid);
	showModal('#modal_process', function(e) {
		$('#modal_process_unity').focus();
	});
}

function editProcess(id) {
	var l = $(id);
	$('#modal_process_title').text('Editar processo');
	$('#modal_process_action').val('edit');
	$('#modal_process_id').val(l.data('id'));
	$('#modal_process_unity').selectpicker('val', l.data('id-unity'));
	$('#modal_process_description').val(l.find('td:nth-child(2)').text());
	$('#modal_process_nup').val(l.data('nup'));
	$('#modal_process_nup').trigger('keyup');
	$('#modal_process_pam').val(l.data('pam'));
	$('#modal_process_tr').val(l.data('tr'));
	$('#modal_process_date').val(l.find('td:nth-child(8)').text());
	$('#modal_process_value').val(l.data('value'));
	$('#modal_process_value').maskMoney('mask');
	$('#modal_process_type').selectpicker('val', l.data('id-type'));
	$('#modal_process_modality').selectpicker('val', l.data('id-modality'));
	$('#modal_process_info').val(l.data('info'));
	$('#modal_process_responsible').selectpicker('val', l.data('id-responsible'));
	showModal('#modal_process', function(e) {
		$('#modal_process_unity').focus();
	});
}

function forwardProcess(id) {
	var l = $(id);
	$('#modal_progress_id').val(l.data('id'));
	showModal('#modal_progress', function(e) {
		$('#modal_progress_date').focus();
	});
}

function backwardProcess(id) {
	var l = $(id);
	$('#modal_regress_process').val(l.data('id'));
	$('#modal_regress_description').val('');
	showModal('#modal_regress', function(e) {
		$('#modal_regress_description').focus();
	});
}

function infoProcess(id) {
	var l = $(id);
	$('#modal_info_process').val(l.data('id'));
	$('#modal_info_status').val(l.data('id-status'));
	$('#modal_info_note').val('');
	showModal('#modal_info', function(e) {
		$('#modal_info_note').focus();
	});
}

function filterProcess(sid) {
	$('.process').each(function(i) {
		if ($(this).data('id-status') == sid) {
			$(this).show(0);
		} else {
			$(this).hide(0);
		}
	});
}

function exportProcess() {
	showModal('#modal_export');
}
