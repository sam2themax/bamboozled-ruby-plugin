package com.alienfast.bamboozled.ruby.tasks.bundler.cli;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.tasks.AbstractBuilderTest;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;

/**
 * Test the rake command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class BundlerCliCommandBuilderTest extends AbstractBuilderTest {

    BundlerCliCommandBuilder bundlerCliCommandBuilder;

    @Override
    @Before
    public void setUp() throws Exception {

        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        this.bundlerCliCommandBuilder = new BundlerCliCommandBuilder(
                getCapabilityContext(),
                getRvmRubyLocator(),
                getRubyRuntime(),
                getRubyExecutablePath() );
    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        this.bundlerCliCommandBuilder.addRubyExecutable();

        assertThat( 1, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = this.bundlerCliCommandBuilder.build().iterator();

        assertThat( getRubyExecutablePath(), equalTo( commandsIterator.next() ) );
    }

    //
    //    @Test
    //    public void testAddIfRakefile() throws Exception {
    //
    //        bundlerCliCommandBuilder.addIfRakeFile(null);
    //        assertThat(0, equalTo(bundlerCliCommandBuilder.build().size()));
    //
    //        bundlerCliCommandBuilder.addIfRakeFile("Rakefile");
    //
    //        assertThat(2, equalTo(bundlerCliCommandBuilder.build().size()));
    //
    //        Iterator<String> commandsIterator = bundlerCliCommandBuilder.build().iterator();
    //
    //        assertThat(BundlerCliCommandBuilder.RAKEFILE_ARG, commandsIterator.next());
    //        assertThat("Rakefile", equalTo(commandsIterator.next()));
    //    }

    @Test
    public void testWithBundleExec() {

        this.bundlerCliCommandBuilder.addBundleExecutable();
        this.bundlerCliCommandBuilder.addIfBundleExec( "true" );

        Iterator<String> commandsIterator = this.bundlerCliCommandBuilder.build().iterator();

        assertThat( RvmFixtures.BUNDLER_PATH, equalTo( commandsIterator.next() ) );
        assertThat( AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG, equalTo( commandsIterator.next() ) );
    }

    @Test
    public void testAddIfVerbose() throws Exception {

        this.bundlerCliCommandBuilder.addIfVerbose( null );
        assertThat( 0, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        this.bundlerCliCommandBuilder.addIfVerbose( "false" );
        assertThat( 0, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        this.bundlerCliCommandBuilder.addIfVerbose( "true" );

        assertThat( 1, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = this.bundlerCliCommandBuilder.build().iterator();

        assertThat( AbstractBundleExecCommandBuilder.VERBOSE_ARG, equalTo( commandsIterator.next() ) );
    }

    @Test
    public void testAddIfTrace() throws Exception {

        this.bundlerCliCommandBuilder.addIfTrace( null );
        assertThat( 0, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        this.bundlerCliCommandBuilder.addIfTrace( "false" );
        assertThat( 0, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        this.bundlerCliCommandBuilder.addIfTrace( "true" );
        assertThat( 1, equalTo( this.bundlerCliCommandBuilder.build().size() ) );

        Iterator<String> commandsIterator = this.bundlerCliCommandBuilder.build().iterator();

        assertThat( BundlerCliCommandBuilder.TRACE_ARG, equalTo( commandsIterator.next() ) );
    }
}
