$(function() {
    var source = new Bloodhound({
      datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.name); },
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: 'http://localhost:4567/pokemon/%QUERY',
        wildcard: '%QUERY'
      },
      limit: 10
    });

    source.initialize();

    $(':checkbox').radiocheck();
    $('select').select2({dropdownCssClass: 'dropdown-inverse'});

    // configure search typeaheads
    $('.typeahead').typeahead(null, {
      name: 'name',
      display: 'numberAndName',
      source: source.ttAdapter()
    });

    $('#pokedexSearch').on('typeahead:selected', function(ev, selection) {
        updatePokedexEntry('#pokedex-entry', selection);
    });

    $('#friendSearch').on('typeahead:selected', function(ev, selection) {
        $('#pokemon1').remove();
        $('#pokemon2').remove();
        $('#isFriend').remove();
        var pokemon1Entry = $('#pokedex-entry').clone();
        var pokemon2Entry = $('#pokedex-entry').clone();
        pokemon1Entry.attr('id', 'pokemon1');
        pokemon2Entry.attr('id', 'pokemon2');
        pokemon1Entry.prependTo($('#friend'));
        updatePokedexEntry('#pokemon1', selection);
        $('#friend').prepend('<h4 id="isFriend">is a good friend of</h4>');

        // findSimilar returns the number of the similar pokemon
        $.get('http://localhost:4567/findSimilar', {number: selection.number}, function(result) {
            // query /pokemon to get pokemon data
            $.get('http://localhost:4567/pokemon/' + result[0].number, function(data) {
                pokemon2Entry.prependTo($('#friend'));
                updatePokedexEntry('#pokemon2', data[0]);
            });
        });

    });

    $('#battleSearch1').on('typeahead:selected', function(ev, selection) {
        $('#pokemon1').remove();
        $('.battleLog').remove();
        var pokemonEntry = $('#pokedex-entry').clone();
        pokemonEntry.attr('id', 'pokemon1');
        pokemonEntry.insertBefore($('#startBattle'));
        pokemon1Number = selection.number;
        updatePokedexEntry('#pokemon1', selection);
    });

    $('#battleSearch2').on('typeahead:selected', function(ev, selection) {
        $('#pokemon2').remove();
        $('.battleLog').remove();
        var pokemonEntry = $('#pokedex-entry').clone();
        pokemonEntry.attr('id', 'pokemon2');
        pokemonEntry.insertBefore($('#startBattle'));
        updatePokedexEntry('#pokemon2', selection);
        pokemon2Number = selection.number;
        $('#startBattle').show();
    });


    // default pokedex entry to Pikachu
    $.get('http://localhost:4567/pokemon/25', function(data) {
        updatePokedexEntry('#pokedex-entry', data[0]);
        $('#pokedex-entry').show();
    });

    // toggle select box colors 
    $(':checkbox').on('change.radiocheck', function() {
        if (document.getElementById('typeCheckbox').checked) {
            $('#typeSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#typeSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('colorCheckbox').checked) {
            $('#colorSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#colorSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('heightCheckbox').checked) {
            $('#heightSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#heightSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('weightCheckbox').checked) {
            $('#weightSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#weightSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('attackCheckbox').checked) {
            $('#attackSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#attackSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('defenseCheckbox').checked) {
            $('#defenseSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#defenseSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('spAttackCheckbox').checked) {
            $('#spAttackSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#spAttackSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('spDefenseCheckbox').checked) {
            $('#spDefenseSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#spDefenseSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('speedCheckbox').checked) {
            $('#speedSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#speedSelection').switchClass('select-primary', 'select-default');
        }
        if (document.getElementById('hpCheckbox').checked) {
            $('#hpSelection').switchClass('select-default', 'select-primary');
        } else {
            $('#hpCheckboxSelection').switchClass('select-primary', 'select-default');
        }
    });
});

function updatePokedexEntry(id, selection) {
    $(id + ' .image').attr('src', '');
    $(id + ' .name-and-number').text(selection.numberAndName);
    $(id + ' .description').text(selection.description);
    $(id + ' .type').text(selection.types);
    $(id + ' .color').text(selection.color.charAt(0).toUpperCase() + selection.color.slice(1));
    $(id + ' .height').text(selection.height / 10 + 'm');
    $(id + ' .weight').text(selection.weight / 10 + 'kg');
    $(id + ' .attack').text(selection.attack);
    $(id + ' .defense').text(selection.defense);
    $(id + ' .sp-attack').text(selection.spAttack);
    $(id + ' .sp-defense').text(selection.spDefense);
    $(id + ' .speed').text(selection.speed);
    $(id + ' .hp').text(selection.hp);
    $(id + ' .image').attr('src', selection.imageUrl);
}

// submit discover query
function discover() {
    $('div[id^="entry"]').remove(); // delete previous results

    queryParams = {};
    if (document.getElementById('typeCheckbox').checked) {
        queryParams.type = $('#typeSelection').val();
    }
    if (document.getElementById('colorCheckbox').checked) {
        queryParams.color = $('#colorSelection').val();
    }
    if (document.getElementById('heightCheckbox').checked) {
        queryParams.heightFilter = $('#heightSelection').val() + ' ' + $('#heightInput').val() * 10;
    }
    if (document.getElementById('weightCheckbox').checked) {
        queryParams.weightFilter = $('#weightSelection').val() + ' ' + $('#weightInput').val() * 10;
    }
    if (document.getElementById('attackCheckbox').checked) {
        queryParams.attackFilter = $('#attackSelection').val() + ' ' + $('#attackInput').val();
    }
    if (document.getElementById('spAttackCheckbox').checked) {
        queryParams.spAttackFilter = $('#spAttackSelection').val() + ' ' + $('#spAttackInput').val();
    }
    if (document.getElementById('defenseCheckbox').checked) {
        queryParams.defenseFilter = $('#defenseSelection').val() + ' ' + $('#defenseInput').val();
    }
    if (document.getElementById('spDefenseCheckbox').checked) {
        queryParams.spDefenseFilter = $('#spDefenseSelection').val() + ' ' + $('#spDefenseInput').val();
    }
    if (document.getElementById('speedCheckbox').checked) {
        queryParams.speedFilter = $('#speedSelection').val() + ' ' + $('#speedInput').val();
    }
    if (document.getElementById('hpCheckbox').checked) {
        queryParams.hpFilter = $('#hpSelection').val() + ' ' + $('#hpInput').val();
    }

    queryParams.sortBy = $('#sortBySelection').val();
    queryParams.sortOrder = $('#orderSelection').val();

    $.get('http://localhost:4567/discover', queryParams, function(data) {
        for (i = data.length - 1; i >= 0; i--) {
            var entryClone = $('#pokedex-entry').clone();
            entryClone.attr('id', 'entry' + i);
            entryClone.prependTo($('#discover'));
            updatePokedexEntry('#entry' + i, data[i]);
        }

    })
}

var pokemon1Number;
var pokemon2Number;

function battle() {
    $.get(
        'http://localhost:4567/battle',
        {
            pokemon1: pokemon1Number,
            pokemon2: pokemon2Number
        },
        function(data) {
            $('#startBattle').before('<p class="battleLog">' + data.battleLog.replace(/\n/g, '<br>') + '</p>');
        }

    )
}


