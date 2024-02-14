 $(document).ready(function() {
        // Function to sort the table based on the clicked column
        function sortTable(columnIndex, ascending) {
            var tbody = $('#gateTableBody');
            var rows = tbody.find('tr').get();
            rows.sort(function(a, b) {
                var aValue = $(a).find('td').eq(columnIndex).text();
                var bValue = $(b).find('td').eq(columnIndex).text();
                return ascending ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
            });
            $.each(rows, function(index, row) {
                tbody.append(row);
            });
        }

        // Event listener for clicking on the th elements in the thead section
        $('th').click(function() {
            var columnIndex = $(this).index();
            var ascending = $(this).data('order') === 'asc';
            sortTable(columnIndex, ascending);
            $(this).data('order', ascending ? 'desc' : 'asc');
            // Reset arrows
            $('th').find('.arrow').remove();
            // Add arrow to indicate sorting direction
            var arrow = ascending ? '&#9650;' : '&#9660;';
            $(this).append('<span class="arrow">' + arrow + '</span>');
        });
    });